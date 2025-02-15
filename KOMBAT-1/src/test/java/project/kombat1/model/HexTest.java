package project.kombat1.model;

import org.junit.jupiter.api.Test;
import project.kombat1.model.Hex;
import project.kombat1.model.Minion;
import project.kombat1.model.Player;

import static org.junit.jupiter.api.Assertions.*;
public class HexTest {

    @Test
    void testGetRow() {
        Hex hex = new Hex(2, 5);
        assertEquals(2, hex.getRow());
    }

    @Test
    void testGetCol() {
        Hex hex = new Hex(2, 5);
        assertEquals(5, hex.getCol());
    }

    @Test
    void testSetMinion() {
        Hex hex = new Hex(2, 5);
        Minion minion = new Minion("Test Minion", 10, 5, "strategy1", hex, new Player("Test Player", 1000), Minion.MinionType.TYPE1);
        hex.setMinion(minion);
        assertEquals(minion, hex.getMinion());
    }

    @Test
    void testSetOwner() {
        Hex hex = new Hex(2, 5);
        Player player = new Player("Test Player", 1000);
        hex.setOwner(player);
        assertEquals(player, hex.getOwner());
    }

    @Test
    void testGetOwner() {
        Hex hex = new Hex(2, 5);
        Player player = new Player("Test Player", 1000);
        hex.setOwner(player);
        assertEquals(player, hex.getOwner());
    }
}