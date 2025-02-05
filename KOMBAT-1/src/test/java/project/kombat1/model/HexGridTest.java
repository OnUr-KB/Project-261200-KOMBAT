package project.kombat1.model;

import org.junit.jupiter.api.Test;
import project.kombat1.model.Hex;
import project.kombat1.model.HexGrid;
import project.kombat1.model.Minion;
import project.kombat1.model.Player;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HexGridTest {

    @Test
    void testGetHex() {
        HexGrid hexGrid = new HexGrid();
        Hex hex = hexGrid.getHex(2, 5);
        assertNotNull(hex);
        assertEquals(2, hex.getRow());
        assertEquals(5, hex.getCol());
    }

    @Test
    void testGetHexOutOfBounds() {
        HexGrid hexGrid = new HexGrid();
        Hex hex = hexGrid.getHex(8, 5);
        assertNull(hex);
    }

    @Test
    void testSetHex() {
        HexGrid hexGrid = new HexGrid();
        Hex hex = new Hex(2, 5);
        hexGrid.setHex(2, 5, hex);
        assertEquals(hex, hexGrid.getHex(2, 5));
    }

    @Test
    void testSetHexOutOfBounds() {
        HexGrid hexGrid = new HexGrid();
        Hex hex = new Hex(8, 5);
        hexGrid.setHex(8, 5, hex);
        assertNull(hexGrid.getHex(8, 5));
    }

    @Test
    void testIsHexEmpty() {
        HexGrid hexGrid = new HexGrid();
        assertTrue(hexGrid.isHexEmpty(2, 5));
    }

    @Test
    void testIsHexNotEmpty() {
        HexGrid hexGrid = new HexGrid();
        Hex hex = new Hex(2, 5);
        Minion minion = new Minion("Test Minion", 10, 5, "strategy1", hex, new Player("Test Player", 1000), Minion.MinionType.TYPE1);
        hex.setMinion(minion);
        hexGrid.setHex(2, 5, hex);
        assertFalse(hexGrid.isHexEmpty(2, 5));
    }

    @Test
    void testGetAdjacentHexes() {
        HexGrid hexGrid = new HexGrid();
        List<Hex> adjacentHexes = hexGrid.getAdjacentHexes(2, 5);
        assertEquals(6, adjacentHexes.size());
    }

    @Test
    void testGetTotalMinionCount() {
        HexGrid hexGrid = new HexGrid();
        assertEquals(0, hexGrid.getTotalMinionCount());
        Hex hex = new Hex(2, 5);
        Minion minion = new Minion("Test Minion", 10, 5, "strategy1", hex, new Player("Test Player", 1000), Minion.MinionType.TYPE1);
        hex.setMinion(minion);
        hexGrid.setHex(2, 5, hex);
        assertEquals(1, hexGrid.getTotalMinionCount());
    }

    @Test
    void testGetAdjacentHexesOwnedByPlayer() {
        HexGrid hexGrid = new HexGrid();
        Player player = new Player("Test Player", 1000);
        Hex hex1 = new Hex(2, 5);
        Hex hex2 = new Hex(3, 5);
        Minion minion1 = new Minion("Test Minion 1", 10, 5, "strategy1", hex1, player, Minion.MinionType.TYPE1);
        Minion minion2 = new Minion("Test Minion 2", 10, 5, "strategy1", hex2, player, Minion.MinionType.TYPE1);
        hex1.setMinion(minion1);
        hex2.setMinion(minion2);
        hexGrid.setHex(2, 5, hex1);
        hexGrid.setHex(3, 5, hex2);
        List<Hex> ownedHexes = hexGrid.getAdjacentHexesOwnedByPlayer(2, 5, player);
        assertEquals(1, ownedHexes.size());
    }
}