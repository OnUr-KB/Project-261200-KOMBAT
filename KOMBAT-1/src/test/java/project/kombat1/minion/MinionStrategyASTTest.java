package project.kombat1.minion;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class MinionStrategyASTTest {

    @Test
    public void testIfStatement() {
        MinionStrategyAST.BinaryExpression condition = new MinionStrategyAST.BinaryExpression(
                MinionStrategyAST.BinaryExpression.Operator.GREATER_THAN,
                new MinionStrategyAST.IdentifierExpression("budget"),
                new MinionStrategyAST.NumberExpression(1000)
        );
        MinionStrategyAST.BlockStatement thenStatement = new MinionStrategyAST.BlockStatement(
                List.of(new MinionStrategyAST.MoveCommand("up"))
        );
        MinionStrategyAST.BlockStatement elseStatement = new MinionStrategyAST.BlockStatement(
                List.of(new MinionStrategyAST.ShootCommand("down", new MinionStrategyAST.NumberExpression(500)))
        );
        MinionStrategyAST.IfStatement ifStatement = new MinionStrategyAST.IfStatement(condition, thenStatement, elseStatement);

        assertEquals(condition, ifStatement.condition);
        assertEquals(thenStatement, ifStatement.thenStatement);
        assertEquals(elseStatement, ifStatement.elseStatement);
    }

    @Test
    public void testWhileStatement() {
        MinionStrategyAST.IdentifierExpression condition = new MinionStrategyAST.IdentifierExpression("opponent");
        MinionStrategyAST.BlockStatement statement = new MinionStrategyAST.BlockStatement(
                List.of(new MinionStrategyAST.MoveCommand("upleft"))
        );
        MinionStrategyAST.WhileStatement whileStatement = new MinionStrategyAST.WhileStatement(condition, statement);

        assertEquals(condition, whileStatement.condition);
        assertEquals(statement, whileStatement.statement);
    }

    @Test
    public void testBlockStatement() {
        List<MinionStrategyAST.Statement> statements = new ArrayList<>();
        statements.add(new MinionStrategyAST.MoveCommand("up"));
        statements.add(new MinionStrategyAST.ShootCommand("down", new MinionStrategyAST.NumberExpression(100)));
        statements.add(new MinionStrategyAST.DoneCommand());
        MinionStrategyAST.BlockStatement blockStatement = new MinionStrategyAST.BlockStatement(statements);

        assertEquals(statements, blockStatement.statements);
    }

    @Test
    public void testAssignmentStatement() {
        MinionStrategyAST.AssignmentStatement assignmentStatement = new MinionStrategyAST.AssignmentStatement(
                "myVar", new MinionStrategyAST.NumberExpression(123)
        );

        assertEquals("myVar", assignmentStatement.identifier);
        assertTrue(assignmentStatement.expression instanceof MinionStrategyAST.NumberExpression);
    }

    @Test
    public void testDoneCommand() {
        MinionStrategyAST.DoneCommand doneCommand = new MinionStrategyAST.DoneCommand();
        assertNotNull(doneCommand);
    }

    @Test
    public void testMoveCommand() {
        MinionStrategyAST.MoveCommand moveCommand = new MinionStrategyAST.MoveCommand("up");
        assertEquals("up", moveCommand.direction);
    }

    @Test
    public void testShootCommand() {
        MinionStrategyAST.ShootCommand shootCommand = new MinionStrategyAST.ShootCommand(
                "down", new MinionStrategyAST.NumberExpression(500)
        );
        assertEquals("down", shootCommand.direction);
        assertTrue(shootCommand.expenditure instanceof MinionStrategyAST.NumberExpression);
    }

    @Test
    public void testBinaryExpression() {
        MinionStrategyAST.BinaryExpression binaryExpression = new MinionStrategyAST.BinaryExpression(
                MinionStrategyAST.BinaryExpression.Operator.PLUS,
                new MinionStrategyAST.IdentifierExpression("budget"),
                new MinionStrategyAST.NumberExpression(100)
        );

        assertEquals(MinionStrategyAST.BinaryExpression.Operator.PLUS, binaryExpression.operator);
        assertTrue(binaryExpression.left instanceof MinionStrategyAST.IdentifierExpression);
        assertTrue(binaryExpression.right instanceof MinionStrategyAST.NumberExpression);
    }

    @Test
    public void testNumberExpression() {
        MinionStrategyAST.NumberExpression numberExpression = new MinionStrategyAST.NumberExpression(123);
        assertEquals(123, numberExpression.value);
    }

    @Test
    public void testIdentifierExpression() {
        MinionStrategyAST.IdentifierExpression identifierExpression = new MinionStrategyAST.IdentifierExpression("budget");
        assertEquals("budget", identifierExpression.identifier);
    }

    @Test
    public void testInfoExpression() {
        MinionStrategyAST.InfoExpression infoExpression = new MinionStrategyAST.InfoExpression(MinionStrategyToken.Type.OPPONENT);
        assertEquals(MinionStrategyToken.Type.OPPONENT, infoExpression.type);
    }

    @Test
    public void testNearbyExpression() {
        MinionStrategyAST.NearbyExpression nearbyExpression = new MinionStrategyAST.NearbyExpression("up");
        assertEquals("up", nearbyExpression.direction);
    }
}