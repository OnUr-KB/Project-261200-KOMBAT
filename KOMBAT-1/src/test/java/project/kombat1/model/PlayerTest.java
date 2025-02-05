package project.kombat1.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void testGetName() {
        Player player = new Player("Test Player", 1000);
        assertEquals("Test Player", player.getName());
    }

    @Test
    void testGetBudget() {
        Player player = new Player("Test Player", 1000);
        assertEquals(1000, player.getBudget());
    }

    @Test
    void testSetBudget() {
        Player player = new Player("Test Player", 1000);
        player.setBudget(2000);
        assertEquals(2000, player.getBudget());
    }
}