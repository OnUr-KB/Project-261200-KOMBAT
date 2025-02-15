package project.kombat1.minion;

import static org.junit.jupiter.api.Assertions.*;
import org.testng.annotations.Test;
import java.util.List;

public class MinionStrategyParserTest {

    @Test
    public void testParseStatement() {
        // IfStatement
        String input1 = "if (budget > 1000) then { move up } else { shoot down 500 }";
        List<MinionStrategyToken> tokens1 = new MinionStrategyLexer(input1).lex();
        MinionStrategyAST.Statement statement1 = new MinionStrategyParser(tokens1).parse();
        assertTrue(statement1 instanceof MinionStrategyAST.IfStatement);

        // WhileStatement
        String input2 = "while (opponent) { move upleft }";
        List<MinionStrategyToken> tokens2 = new MinionStrategyLexer(input2).lex();
        MinionStrategyAST.Statement statement2 = new MinionStrategyParser(tokens2).parse();
        assertTrue(statement2 instanceof MinionStrategyAST.WhileStatement);

        // BlockStatement
        String input3 = "{ move up shoot down 100 done }";
        List<MinionStrategyToken> tokens3 = new MinionStrategyLexer(input3).lex();
        MinionStrategyAST.Statement statement3 = new MinionStrategyParser(tokens3).parse();
        assertTrue(statement3 instanceof MinionStrategyAST.BlockStatement);

        // AssignmentStatement
        String input4 = "myVar = 123";
        List<MinionStrategyToken> tokens4 = new MinionStrategyLexer(input4).lex();
        MinionStrategyAST.Statement statement4 = new MinionStrategyParser(tokens4).parse();
        assertTrue(statement4 instanceof MinionStrategyAST.AssignmentStatement);

        // ActionCommand
        String input5 = "done";
        List<MinionStrategyToken> tokens5 = new MinionStrategyLexer(input5).lex();
        MinionStrategyAST.Statement statement5 = new MinionStrategyParser(tokens5).parse();
        assertTrue(statement5 instanceof MinionStrategyAST.DoneCommand);
    }

    @Test
    public void testParseIfStatement() {
        String input = "if (budget > 1000) then { move up } else { shoot down 500 }";
        List<MinionStrategyToken> tokens = new MinionStrategyLexer(input).lex();
        MinionStrategyAST.IfStatement statement = (MinionStrategyAST.IfStatement) new MinionStrategyParser(tokens).parse();
        assertTrue(statement.condition instanceof MinionStrategyAST.BinaryExpression);
        assertTrue(statement.thenStatement instanceof MinionStrategyAST.BlockStatement);
        assertTrue(statement.elseStatement instanceof MinionStrategyAST.BlockStatement);
    }

    @Test
    public void testParseWhileStatement() {
        String input = "while (opponent) { move upleft }";
        List<MinionStrategyToken> tokens = new MinionStrategyLexer(input).lex();
        MinionStrategyAST.WhileStatement statement = (MinionStrategyAST.WhileStatement) new MinionStrategyParser(tokens).parse();
        assertTrue(statement.condition instanceof MinionStrategyAST.IdentifierExpression);
        assertTrue(statement.statement instanceof MinionStrategyAST.BlockStatement);
    }

    @Test
    public void testParseBlockStatement() {
        String input = "{ move up shoot down 100 done }";
        List<MinionStrategyToken> tokens = new MinionStrategyLexer(input).lex();
        MinionStrategyAST.BlockStatement statement = (MinionStrategyAST.BlockStatement) new MinionStrategyParser(tokens).parse();
        assertEquals(3, statement.statements.size());
        assertTrue(statement.statements.get(0) instanceof MinionStrategyAST.MoveCommand);
        assertTrue(statement.statements.get(1) instanceof MinionStrategyAST.ShootCommand);
        assertTrue(statement.statements.get(2) instanceof MinionStrategyAST.DoneCommand);
    }

    @Test
    public void testParseAssignmentStatement() {
        String input = "myVar = 123";
        List<MinionStrategyToken> tokens = new MinionStrategyLexer(input).lex();
        MinionStrategyAST.AssignmentStatement statement = (MinionStrategyAST.AssignmentStatement) new MinionStrategyParser(tokens).parse();
        assertEquals("myVar", statement.identifier);
        assertTrue(statement.expression instanceof MinionStrategyAST.NumberExpression);
    }

    @Test
    public void testParseActionCommand() {
        // DoneCommand
        String input1 = "done";
        List<MinionStrategyToken> tokens1 = new MinionStrategyLexer(input1).lex();
        MinionStrategyAST.Statement statement1 = new MinionStrategyParser(tokens1).parse();
        assertTrue(statement1 instanceof MinionStrategyAST.DoneCommand);

        // MoveCommand
        String input2 = "move upleft";
        List<MinionStrategyToken> tokens2 = new MinionStrategyLexer(input2).lex();
        MinionStrategyAST.Statement statement2 = new MinionStrategyParser(tokens2).parse();
        assertTrue(statement2 instanceof MinionStrategyAST.MoveCommand);

        // ShootCommand
        String input3 = "shoot down 500";
        List<MinionStrategyToken> tokens3 = new MinionStrategyLexer(input3).lex();
        MinionStrategyAST.Statement statement3 = new MinionStrategyParser(tokens3).parse();
        assertTrue(statement3 instanceof MinionStrategyAST.ShootCommand);
    }

    @Test
    public void testParseExpression() {
        // BinaryExpression
        String input1 = "budget + 100 - 50 * 2 / 5 % 3 ^ 2";
        List<MinionStrategyToken> tokens1 = new MinionStrategyLexer(input1).lex();
        MinionStrategyAST.Expression expression1 = new MinionStrategyParser(tokens1).parseExpression();
        assertTrue(expression1 instanceof MinionStrategyAST.BinaryExpression);

        // NumberExpression
        String input2 = "123";
        List<MinionStrategyToken> tokens2 = new MinionStrategyLexer(input2).lex();
        MinionStrategyAST.Expression expression2 = new MinionStrategyParser(tokens2).parseExpression();
        assertTrue(expression2 instanceof MinionStrategyAST.NumberExpression);

        // IdentifierExpression
        String input3 = "budget";
        List<MinionStrategyToken> tokens3 = new MinionStrategyLexer(input3).lex();
        MinionStrategyAST.Expression expression3 = new MinionStrategyParser(tokens3).parseExpression();
        assertTrue(expression3 instanceof MinionStrategyAST.IdentifierExpression);

        // InfoExpression
        String input4 = "opponent";
        List<MinionStrategyToken> tokens4 = new MinionStrategyLexer(input4).lex();
        MinionStrategyAST.Expression expression4 = new MinionStrategyParser(tokens4).parseExpression();
        assertTrue(expression4 instanceof MinionStrategyAST.InfoExpression);

        // NearbyExpression
        String input5 = "nearby up";
        List<MinionStrategyToken> tokens5 = new MinionStrategyLexer(input5).lex();
        MinionStrategyAST.Expression expression5 = new MinionStrategyParser(tokens5).parseExpression();
        assertTrue(expression5 instanceof MinionStrategyAST.NearbyExpression);
    }

    // เพิ่มเติม Test Case สำหรับ method อื่นๆ ตามต้องการ
}