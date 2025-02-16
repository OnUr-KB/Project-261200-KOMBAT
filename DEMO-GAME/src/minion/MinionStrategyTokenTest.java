package main.minion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;

class MinionStrategyTokenTest {

    @Test
    void testTokenCreation() {
        MinionStrategyToken token = new MinionStrategyToken(MinionStrategyToken.Type.PLUS, "+");

        assertEquals(MinionStrategyToken.Type.PLUS, token.type);
        assertEquals("+", token.lexeme);
    }

    @Test
    void testEqualsAndHashCode() {
        MinionStrategyToken token1 = new MinionStrategyToken(MinionStrategyToken.Type.PLUS, "+");
        MinionStrategyToken token2 = new MinionStrategyToken(MinionStrategyToken.Type.PLUS, "+");
        MinionStrategyToken token3 = new MinionStrategyToken(MinionStrategyToken.Type.MINUS, "-");

        assertEquals(token1, token2, "Tokens with the same type and lexeme should be equal.");
        assertNotEquals(token1, token3, "Different tokens should not be equal.");

        assertEquals(token1.hashCode(), token2.hashCode(), "Equal tokens should have the same hash code.");
    }

    @Test
    void testHashSetBehavior() {
        Set<MinionStrategyToken> tokenSet = new HashSet<>();
        MinionStrategyToken token1 = new MinionStrategyToken(MinionStrategyToken.Type.PLUS, "+");
        MinionStrategyToken token2 = new MinionStrategyToken(MinionStrategyToken.Type.PLUS, "+");

        tokenSet.add(token1);
        tokenSet.add(token2);

        assertEquals(1, tokenSet.size(), "HashSet should only contain one instance of equal tokens.");
    }

    @Test
    void testToString() {
        MinionStrategyToken token = new MinionStrategyToken(MinionStrategyToken.Type.MINUS, "-");

        String expectedString = "Token{type=MINUS, lexeme='-'}";
        assertEquals(expectedString, token.toString(), "toString() output should match the expected format.");
    }
}
