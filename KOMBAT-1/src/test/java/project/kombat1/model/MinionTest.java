package project.kombat1.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MinionTest {

    @Test
    void testGetName() {
        Hex hex = new Hex(0, 0);
        Player owner = new Player("Test Player", 1000);
        Minion minion = new Minion("Test Minion", 10, 5, "strategy1", hex, owner, Minion.MinionType.TYPE1);
        assertEquals("Test Minion", minion.getName());
    }

    @Test
    void testGetAttack() {
        Hex hex = new Hex(0, 0);
        Player owner = new Player("Test Player", 1000);
        Minion minion = new Minion("Test Minion", 10, 5, "strategy1", hex, owner, Minion.MinionType.TYPE1);
        assertEquals(10, minion.getAttack());
    }

    @Test
    void testGetDefense() {
        Hex hex = new Hex(0, 0);
        Player owner = new Player("Test Player", 1000);
        Minion minion = new Minion("Test Minion", 10, 5, "strategy1", hex, owner, Minion.MinionType.TYPE1);
        assertEquals(5, minion.getDefense());
    }

    @Test
    void testGetStrategy() {
        Hex hex = new Hex(0, 0);
        Player owner = new Player("Test Player", 1000);
        Minion minion = new Minion("Test Minion", 10, 5, "strategy1", hex, owner, Minion.MinionType.TYPE1);
        assertEquals("strategy1", minion.getStrategy());
    }

    @Test
    void testGetHex() {
        Hex hex = new Hex(0, 0);
        Player owner = new Player("Test Player", 1000);
        Minion minion = new Minion("Test Minion", 10, 5, "strategy1", hex, owner, Minion.MinionType.TYPE1);
        assertEquals(hex, minion.getHex());
    }

    @Test
    void testSetHex() {
        Hex hex1 = new Hex(0, 0);
        Hex hex2 = new Hex(1, 1);
        Player owner = new Player("Test Player", 1000);
        Minion minion = new Minion("Test Minion", 10, 5, "strategy1", hex1, owner, Minion.MinionType.TYPE1);
        minion.setHex(hex2);
        assertEquals(hex2, minion.getHex());
    }

    @Test
    void testGetOwner() {
        Hex hex = new Hex(0, 0);
        Player owner = new Player("Test Player", 1000);
        Minion minion = new Minion("Test Minion", 10, 5, "strategy1", hex, owner, Minion.MinionType.TYPE1);
        assertEquals(owner, minion.getOwner());
    }

    @Test
    void testGetHp() {
        Hex hex = new Hex(0, 0);
        Player owner = new Player("Test Player", 1000);
        Minion minion = new Minion("Test Minion", 10, 5, "strategy1", hex, owner, Minion.MinionType.TYPE1);
        assertEquals(0, minion.getHp()); // เริ่มต้น hp = 0
    }

    @Test
    void testSetHp() {
        Hex hex = new Hex(0, 0);
        Player owner = new Player("Test Player", 1000);
        Minion minion = new Minion("Test Minion", 10, 5, "strategy1", hex, owner, Minion.MinionType.TYPE1);
        minion.setHp(50);
        assertEquals(50, minion.getHp());
    }

    @Test
    void testGetType() {
        Hex hex = new Hex(0, 0);
        Player owner = new Player("Test Player", 1000);
        Minion minion = new Minion("Test Minion", 10, 5, "strategy1", hex, owner, Minion.MinionType.TYPE1);
        assertEquals(Minion.MinionType.TYPE1, minion.getType());
    }

    @Test
    void testIsCurrent() {
        Hex hex = new Hex(0, 0);
        Player owner = new Player("Test Player", 1000);
        Minion minion = new Minion("Test Minion", 10, 5, "strategy1", hex, owner, Minion.MinionType.TYPE1);
        assertFalse(minion.isCurrent()); // เริ่มต้น current = false
    }
}