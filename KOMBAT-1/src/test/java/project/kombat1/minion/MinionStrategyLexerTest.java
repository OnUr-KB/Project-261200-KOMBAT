package project.kombat1.minion;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
import java.util.List;

public class MinionStrategyLexerTest {

    @Test
    public void testLexNumbers() {
        String input = "123 456 789";
        List<MinionStrategyToken> tokens = new MinionStrategyLexer(input).lex();
        assertEquals(3, tokens.size());
        assertEquals(MinionStrategyToken.Type.NUMBER, tokens.get(0).type);
        assertEquals("123", tokens.get(0).lexeme);
        assertEquals(MinionStrategyToken.Type.NUMBER, tokens.get(1).type);
        assertEquals("456", tokens.get(1).lexeme);
        assertEquals(MinionStrategyToken.Type.NUMBER, tokens.get(2).type);
        assertEquals("789", tokens.get(2).lexeme);
    }

    @Test
    public void testLexIdentifiers() {
        String input = "budget myVar opponent";
        List<MinionStrategyToken> tokens = new MinionStrategyLexer(input).lex();
        assertEquals(3, tokens.size());
        assertEquals(MinionStrategyToken.Type.IDENTIFIER, tokens.get(0).type);
        assertEquals("budget", tokens.get(0).lexeme);
        assertEquals(MinionStrategyToken.Type.IDENTIFIER, tokens.get(1).type);
        assertEquals("myVar", tokens.get(1).lexeme);
        assertEquals(MinionStrategyToken.Type.OPPONENT, tokens.get(2).type);
        assertEquals("opponent", tokens.get(2).lexeme);
    }

    @Test
    public void testLexKeywords() {
        String input = "if then else while done move shoot up down upleft upright downleft downright nearby ally opponent";
        List<MinionStrategyToken> tokens = new MinionStrategyLexer(input).lex();
        assertEquals(15, tokens.size());
        assertEquals(MinionStrategyToken.Type.IF, tokens.get(0).type);
        assertEquals(MinionStrategyToken.Type.THEN, tokens.get(1).type);
        assertEquals(MinionStrategyToken.Type.ELSE, tokens.get(2).type);
        assertEquals(MinionStrategyToken.Type.WHILE, tokens.get(3).type);
        assertEquals(MinionStrategyToken.Type.DONE, tokens.get(4).type);
        assertEquals(MinionStrategyToken.Type.MOVE, tokens.get(5).type);
        assertEquals(MinionStrategyToken.Type.SHOOT, tokens.get(6).type);
        assertEquals(MinionStrategyToken.Type.UP, tokens.get(7).type);
        assertEquals(MinionStrategyToken.Type.DOWN, tokens.get(8).type);
        assertEquals(MinionStrategyToken.Type.UPLEFT, tokens.get(9).type);
        assertEquals(MinionStrategyToken.Type.UPRIGHT, tokens.get(10).type);
        assertEquals(MinionStrategyToken.Type.DOWNLEFT, tokens.get(11).type);
        assertEquals(MinionStrategyToken.Type.DOWNRIGHT, tokens.get(12).type);
        assertEquals(MinionStrategyToken.Type.NEARBY, tokens.get(13).type);
        assertEquals(MinionStrategyToken.Type.ALLY, tokens.get(14).type);
        assertEquals(MinionStrategyToken.Type.OPPONENT, tokens.get(15).type);
    }

    @Test
    public void testLexOperators() {
        String input = "= + - * / % ( ) { } $ ^";
        List<MinionStrategyToken> tokens = new MinionStrategyLexer(input).lex();
        assertEquals(11, tokens.size());
        assertEquals(MinionStrategyToken.Type.ASSIGN, tokens.get(0).type);
        assertEquals(MinionStrategyToken.Type.PLUS, tokens.get(1).type);
        assertEquals(MinionStrategyToken.Type.MINUS, tokens.get(2).type);
        assertEquals(MinionStrategyToken.Type.MULTIPLY, tokens.get(3).type);
        assertEquals(MinionStrategyToken.Type.DIVIDE, tokens.get(4).type);
        assertEquals(MinionStrategyToken.Type.MODULO, tokens.get(5).type);
        assertEquals(MinionStrategyToken.Type.LPAREN, tokens.get(6).type);
        assertEquals(MinionStrategyToken.Type.RPAREN, tokens.get(7).type);
        assertEquals(MinionStrategyToken.Type.LBRACE, tokens.get(8).type);
        assertEquals(MinionStrategyToken.Type.RBRACE, tokens.get(9).type);
        assertEquals(MinionStrategyToken.Type.DOLLAR, tokens.get(10).type);
        assertEquals(MinionStrategyToken.Type.POWER, tokens.get(11).type);
    }

    @Test
    public void testLexComplexInput() {
        String input = "if (budget > 1000) then { move up } else { shoot down 500 }";
        List<MinionStrategyToken> tokens = new MinionStrategyLexer(input).lex();
        assertEquals(17, tokens.size());
        assertEquals(MinionStrategyToken.Type.IF, tokens.get(0).type);
        assertEquals(MinionStrategyToken.Type.LPAREN, tokens.get(1).type);
        //... ตรวจสอบ token อื่นๆ
    }

    @Test(expected = RuntimeException.class)
    public void testLexInvalidInput() {
        String input = "!";
        new MinionStrategyLexer(input).lex();
    }
}