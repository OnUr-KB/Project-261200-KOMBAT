package kombat.project1_1.service;

import kombat.project1_1.model.*;
import kombat.project1_1.player.Cheesebear;
import kombat.project1_1.player.Moonum;
import kombat.project1_1.player.Pandy;
import kombat.project1_1.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    private GameService gameService;
    private GameState gameState;
    private Player player1;
    private Player player2;
    private HexGrid hexGrid;

    @BeforeEach
    public void setUp() {
        gameService = new GameService();
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        hexGrid = new HexGrid();
        gameState = new GameState(player1, player2, hexGrid, 10);
        gameService.startGame(player1, player2, 10);
    }

    @Test
    public void testBuyHex_SuccessfulPurchase() {
        Hex hex = hexGrid.getHex(0, 0);
        int cost = 5000;
        assertTrue(gameService.buyHex(player1, hex, cost));
        assertEquals(player1.getCurrentMoney(), 100000 - cost);
        assertEquals(hex.getOwner(), player1);
        assertTrue(player1.getOwnedHexes().contains(hex));
    }

    @Test
    public void testBuyHex_InsufficientFunds() {
        Hex hex = hexGrid.getHex(0, 0);
        int cost = 150000;
        assertFalse(gameService.buyHex(player1, hex, cost));
        assertEquals(player1.getCurrentMoney(), 100000);
        assertNull(hex.getOwner());
        assertFalse(player1.getOwnedHexes().contains(hex));
    }

    @Test
    public void testBuyHex_HexOccupied() {
        Hex hex = hexGrid.getHex(0, 0);
        hex.setMinion(new Cheesebear());
        int cost = 5000;
        assertFalse(gameService.buyHex(player1, hex, cost));
        assertEquals(player1.getCurrentMoney(), 100000);
        assertNull(hex.getOwner());
        assertFalse(player1.getOwnedHexes().contains(hex));
    }

    @Test
    public void testBuyHex_HexOwned() {
        Hex hex = hexGrid.getHex(0, 0);
        hex.setOwner(player2);
        int cost = 5000;
        assertFalse(gameService.buyHex(player1, hex, cost));
        assertEquals(player1.getCurrentMoney(), 100000);
        assertEquals(hex.getOwner(), player2);
        assertFalse(player1.getOwnedHexes().contains(hex));
    }

    @Test
    public void testPlaceMinion_SuccessfulPlacement() {
        Hex hex = hexGrid.getHex(0, 0);
        hex.setOwner(player1);
        Minion minion = new Cheesebear();
        assertTrue(gameService.placeMinion(player1, minion, hex));
        assertEquals(player1.getCurrentMoney(), 100000 - minion.getCost());
        assertEquals(hex.getMinion(), minion);
        assertTrue(player1.getMinions().contains(minion));
    }

    @Test
    public void testPlaceMinion_HexNotOwned() {
        Hex hex = hexGrid.getHex(0, 0);
        Minion minion = new Cheesebear();
        assertFalse(gameService.placeMinion(player1, minion, hex));
        assertEquals(player1.getCurrentMoney(), 100000);
        assertNull(hex.getMinion());
        assertFalse(player1.getMinions().contains(minion));
    }

    @Test
    public void testPlaceMinion_HexOccupied() {
        Hex hex = hexGrid.getHex(0, 0);
        hex.setOwner(player1);
        hex.setMinion(new Moonum());
        Minion minion = new Cheesebear();
        assertFalse(gameService.placeMinion(player1, minion, hex));
        assertEquals(player1.getCurrentMoney(), 100000);
        assertNotEquals(hex.getMinion(), minion);
        assertFalse(player1.getMinions().contains(minion));
    }

    @Test
    public void testPlaceMinion_InsufficientFunds() {
        Hex hex = hexGrid.getHex(0, 0);
        hex.setOwner(player1);
        Minion minion = new Moonum(); // Expensive minion
        player1.setCurrentMoney(10000);
        assertFalse(gameService.placeMinion(player1, minion, hex));
        assertEquals(player1.getCurrentMoney(), 10000);
        assertNull(hex.getMinion());
        assertFalse(player1.getMinions().contains(minion));
    }

    @Test
    public void testAttack_SuccessfulAttack() {
        Minion attacker = new Cheesebear();
        Minion target = new Pandy();
        player1.setCurrentMoney(attacker.getAttackCost());
        assertTrue(gameService.attack(player1, attacker, target));
        assertEquals(player1.getCurrentMoney(), 0);
        assertNotEquals(target.getCurrentHealth(), target.getMaxHealth());
    }

    @Test
    public void testAttack_InsufficientFunds() {
        Minion attacker = new Cheesebear();
        Minion target = new Pandy();
        assertFalse(gameService.attack(player1, attacker, target));
        assertEquals(player1.getCurrentMoney(), 100000);
        assertEquals(target.getCurrentHealth(), target.getMaxHealth());
    }

    @Test
    public void testAttack_AttackingMinionDead() {
        Minion attacker = new Cheesebear();
        Minion target = new Pandy();
        player1.setCurrentMoney(attacker.getAttackCost());
        assertFalse(gameService.attack(player1, attacker, target));
        assertEquals(player1.getCurrentMoney(), attacker.getAttackCost());
        assertEquals(target.getCurrentHealth(), target.getMaxHealth());
    }

    @Test
    public void testAttack_TargetMinionDead() {
        Minion attacker = new Cheesebear();
        Minion target = new Pandy();
        player1.setCurrentMoney(attacker.getAttackCost());
        assertFalse(gameService.attack(player1, attacker, target));
        assertEquals(player1.getCurrentMoney(), attacker.getAttackCost());
    }

    @Test
    public void testEndTurn() {
        GameState gameStateMock = Mockito.mock(GameState.class);
        gameService.startGame(player1, player2, 10);
        gameService.endTurn();
        verify(gameStateMock, times(1)).nextTurn();
    }

    @Test
    public void testIsGameOver_GameOver() {
        when(gameState.isGameOver()).thenReturn(true);
        assertTrue(gameService.isGameOver());
    }

    @Test
    public void testIsGameOver_NotOver() {
        when(gameState.isGameOver()).thenReturn(false);
        assertFalse(gameService.isGameOver());
    }

    @Test
    public void testDetermineWinner_Winner() {
        when(gameState.determineWinner()).thenReturn(player1);
        assertEquals(player1, gameService.determineWinner());
    }

    @Test
    public void testDetermineWinner_NoWinner() {
        when(gameState.determineWinner()).thenReturn(null);
        assertNull(gameService.determineWinner());
    }

    @Test
    public void testGetGameState() {
        assertEquals(gameState, gameService.getGameState());
    }
}