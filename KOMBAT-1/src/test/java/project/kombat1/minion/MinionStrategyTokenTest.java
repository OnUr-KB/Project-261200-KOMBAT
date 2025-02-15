package project.kombat1.minion;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;

public class MinionStrategyTokenTest {

    @Test
    public void testConstructor() {
        // ทดสอบสร้าง Token แต่ละประเภท
        MinionStrategyToken token1 = new MinionStrategyToken(MinionStrategyToken.Type.NUMBER, "123");
        assertEquals(MinionStrategyToken.Type.NUMBER, token1.type);
        assertEquals("123", token1.lexeme);

        MinionStrategyToken token2 = new MinionStrategyToken(MinionStrategyToken.Type.IDENTIFIER, "myVar");
        assertEquals(MinionStrategyToken.Type.IDENTIFIER, token2.type);
        assertEquals("myVar", token2.lexeme);

        MinionStrategyToken token3 = new MinionStrategyToken(MinionStrategyToken.Type.IF, "if");
        assertEquals(MinionStrategyToken.Type.IF, token3.type);
        assertEquals("if", token3.lexeme);

        //... ทดสอบ Token ประเภทอื่นๆ
    }

    @Test
    public void testToString() {
        // ทดสอบ method toString()
        MinionStrategyToken token = new MinionStrategyToken(MinionStrategyToken.Type.NUMBER, "123");
        assertEquals("Token{type=NUMBER, lexeme='123'}", token.toString());
    }

    @Test
    public void testEquals() {
        // ทดสอบ method equals()
        MinionStrategyToken token1 = new MinionStrategyToken(MinionStrategyToken.Type.NUMBER, "123");
        MinionStrategyToken token2 = new MinionStrategyToken(MinionStrategyToken.Type.NUMBER, "123");
        MinionStrategyToken token3 = new MinionStrategyToken(MinionStrategyToken.Type.IDENTIFIER, "123");

        assertTrue(token1.equals(token2));
        assertFalse(token1.equals(token3));
    }
}