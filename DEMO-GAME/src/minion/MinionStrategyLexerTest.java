package main.minion;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MinionStrategyLexerTest {

    @Test
    void testLexNumbers() {
        MinionStrategyLexer lexer = new MinionStrategyLexer("123 456 7890");
        List<MinionStrategyToken> tokens = lexer.lex();

        assertEquals(List.of(
                new MinionStrategyToken(MinionStrategyToken.Type.NUMBER, "123"),
                new MinionStrategyToken(MinionStrategyToken.Type.NUMBER, "456"),
                new MinionStrategyToken(MinionStrategyToken.Type.NUMBER, "7890")
        ), tokens);
    }

    @Test
    void testLexOperators() {
        MinionStrategyLexer lexer = new MinionStrategyLexer("= + - * / % ^");
        List<MinionStrategyToken> tokens = lexer.lex();

        List<MinionStrategyToken> expectedTokens = List.of(
                new MinionStrategyToken(MinionStrategyToken.Type.ASSIGN, "="),
                new MinionStrategyToken(MinionStrategyToken.Type.PLUS, "+"),
                new MinionStrategyToken(MinionStrategyToken.Type.MINUS, "-"),
                new MinionStrategyToken(MinionStrategyToken.Type.MULTIPLY, "*"),
                new MinionStrategyToken(MinionStrategyToken.Type.DIVIDE, "/"),
                new MinionStrategyToken(MinionStrategyToken.Type.MODULO, "%"),
                new MinionStrategyToken(MinionStrategyToken.Type.POWER, "^")
        );

        assertIterableEquals(expectedTokens, tokens);
    }

    @Test
    void testLexParenthesesAndBraces() {
        MinionStrategyLexer lexer = new MinionStrategyLexer("( ) { }");
        List<MinionStrategyToken> tokens = lexer.lex();

        assertEquals(List.of(
                new MinionStrategyToken(MinionStrategyToken.Type.LPAREN, "("),
                new MinionStrategyToken(MinionStrategyToken.Type.RPAREN, ")"),
                new MinionStrategyToken(MinionStrategyToken.Type.LBRACE, "{"),
                new MinionStrategyToken(MinionStrategyToken.Type.RBRACE, "}")
        ), tokens);
    }

    @Test
    void testLexKeywordsAndIdentifiers() {
        MinionStrategyLexer lexer = new MinionStrategyLexer("if then else while move shoot nearby ally opponent");
        List<MinionStrategyToken> tokens = lexer.lex();

        assertEquals(List.of(
                new MinionStrategyToken(MinionStrategyToken.Type.IF, "if"),
                new MinionStrategyToken(MinionStrategyToken.Type.THEN, "then"),
                new MinionStrategyToken(MinionStrategyToken.Type.ELSE, "else"),
                new MinionStrategyToken(MinionStrategyToken.Type.WHILE, "while"),
                new MinionStrategyToken(MinionStrategyToken.Type.MOVE, "move"),
                new MinionStrategyToken(MinionStrategyToken.Type.SHOOT, "shoot"),
                new MinionStrategyToken(MinionStrategyToken.Type.NEARBY, "nearby"),
                new MinionStrategyToken(MinionStrategyToken.Type.ALLY, "ally"),
                new MinionStrategyToken(MinionStrategyToken.Type.OPPONENT, "opponent")
        ), tokens);
    }

    @Test
    void testLexDirections() {
        MinionStrategyLexer lexer = new MinionStrategyLexer("up down upleft upright downleft downright");
        List<MinionStrategyToken> tokens = lexer.lex();

        assertEquals(List.of(
                new MinionStrategyToken(MinionStrategyToken.Type.UP, "up"),
                new MinionStrategyToken(MinionStrategyToken.Type.DOWN, "down"),
                new MinionStrategyToken(MinionStrategyToken.Type.UPLEFT, "upleft"),
                new MinionStrategyToken(MinionStrategyToken.Type.UPRIGHT, "upright"),
                new MinionStrategyToken(MinionStrategyToken.Type.DOWNLEFT, "downleft"),
                new MinionStrategyToken(MinionStrategyToken.Type.DOWNRIGHT, "downright")
        ), tokens);
    }

    @Test
    void testLexComplexExpression() {
        MinionStrategyLexer lexer = new MinionStrategyLexer("move up + 5 * (3 - 2)");
        List<MinionStrategyToken> tokens = lexer.lex();

        assertEquals(List.of(
                new MinionStrategyToken(MinionStrategyToken.Type.MOVE, "move"),
                new MinionStrategyToken(MinionStrategyToken.Type.UP, "up"),
                new MinionStrategyToken(MinionStrategyToken.Type.PLUS, "+"),
                new MinionStrategyToken(MinionStrategyToken.Type.NUMBER, "5"),
                new MinionStrategyToken(MinionStrategyToken.Type.MULTIPLY, "*"),
                new MinionStrategyToken(MinionStrategyToken.Type.LPAREN, "("),
                new MinionStrategyToken(MinionStrategyToken.Type.NUMBER, "3"),
                new MinionStrategyToken(MinionStrategyToken.Type.MINUS, "-"),
                new MinionStrategyToken(MinionStrategyToken.Type.NUMBER, "2"),
                new MinionStrategyToken(MinionStrategyToken.Type.RPAREN, ")")
        ), tokens);
    }

    @Test
    void testUnexpectedCharacter() {
        MinionStrategyLexer lexer = new MinionStrategyLexer("&");
        Exception exception = assertThrows(RuntimeException.class, lexer::lex);
        assertTrue(exception.getMessage().contains("Unexpected character: &"));
    }
}
