import kombat.project1_1.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameStateTest {

    private GameState gameState;
    private Player player1;
    private Player player2;
    private HexGrid hexGrid;

    @BeforeEach
    public void setUp() {
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        hexGrid = mock(HexGrid.class);
        gameState = new GameState(player1, player2, hexGrid, 10);
    }

    @Test
    public void testNextTurn() {
        gameState.nextTurn();
        assertEquals(2, gameState.getCurrentTurn());
        // ตรวจสอบว่าเงินของผู้เล่นเพิ่มขึ้น
        // ตรวจสอบว่าดอกเบี้ยถูกคำนวณ
        // ตรวจสอบว่า executeMinionStrategies() ถูกเรียก
    }

    @Test
    public void testIsGameOver_MaxTurnsReached() {
        gameState.nextTurn(); // currentTurn = 2
        gameState.nextTurn(); // currentTurn = 3
        //... ไปเรื่อยๆ จนถึง currentTurn = 11
        assertTrue(gameState.isGameOver());
    }

    @Test
    public void testIsGameOver_Player1HasNoMinions() {
        when(player1.hasMinionsAlive()).thenReturn(false);
        assertTrue(gameState.isGameOver());
    }

    @Test
    public void testIsGameOver_Player2HasNoMinions() {
        when(player2.hasMinionsAlive()).thenReturn(false);
        assertTrue(gameState.isGameOver());
    }

    @Test
    public void testDetermineWinner_Player1Wins() {
        when(player1.hasMinionsAlive()).thenReturn(true);
        when(player2.hasMinionsAlive()).thenReturn(false);
        assertEquals(player1, gameState.determineWinner());
    }

    @Test
    public void testDetermineWinner_Player2Wins() {
        when(player1.hasMinionsAlive()).thenReturn(false);
        when(player2.hasMinionsAlive()).thenReturn(true);
        assertEquals(player2, gameState.determineWinner());
    }

    @Test
    public void testDetermineWinner_Draw() {
        when(player1.hasMinionsAlive()).thenReturn(true);
        when(player2.hasMinionsAlive()).thenReturn(true);
        List<Minion> minions1 = new ArrayList<>();
        minions1.add(mock(Minion.class));
        when(player1.getMinions()).thenReturn(minions1);
        List<Minion> minions2 = new ArrayList<>();
        minions2.add(mock(Minion.class));
        when(player2.getMinions()).thenReturn(minions2);
        assertNull(gameState.determineWinner());
    }

    @Test
    public void testGetHexOfMinion() {
        Minion minion = mock(Minion.class);
        Hex hex = mock(Hex.class);
        when(hex.getMinion()).thenReturn(minion);
        when(hexGrid.getGrid()).thenReturn(new Hex{{hex}});
        assertEquals(hex, gameState.getHexOfMinion(minion));
    }

    @Test
    public void testFindNearestEnemy() {
        Minion minion1 = mock(Minion.class);
        Minion minion2 = mock(Minion.class);
        Hex hex1 = mock(Hex.class);
        Hex hex2 = mock(Hex.class);
        when(hex1.getX()).thenReturn(0);
        when(hex1.getY()).thenReturn(0);
        when(hex2.getX()).thenReturn(1);
        when(hex2.getY()).thenReturn(1);
        when(minion1.getOwner()).thenReturn(player1);
        when(minion2.getOwner()).thenReturn(player2);
        when(hexGrid.getGrid()).thenReturn(new Hex{{hex1}, {hex2}});
        when(player1.getMinions()).thenReturn(List.of(minion1));
        when(player2.getMinions()).thenReturn(List.of(minion2));
        when(gameState.getHexOfMinion(minion1)).thenReturn(hex1);
        when(gameState.getHexOfMinion(minion2)).thenReturn(hex2);
        assertEquals(minion2, gameState.findNearestEnemy(minion1));
    }

    @Test
    public void testGetNextStepTowards() {
        Hex from = mock(Hex.class);
        Hex to = mock(Hex.class);
        Hex nextStep = mock(Hex.class);
        when(from.getX()).thenReturn(0);
        when(from.getY()).thenReturn(0);
        when(to.getX()).thenReturn(2);
        when(to.getY()).thenReturn(2);
        when(nextStep.getX()).thenReturn(1);
        when(nextStep.getY()).thenReturn(1);
        when(hexGrid.getAdjacentHexes(from)).thenReturn(List.of(nextStep));
        assertEquals(nextStep, gameState.getNextStepTowards(from, to));
    }

}