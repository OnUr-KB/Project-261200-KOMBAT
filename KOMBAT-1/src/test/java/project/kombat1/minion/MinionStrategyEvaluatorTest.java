package project.kombat1.minion;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import project.kombat1.model.GameState;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinionStrategyEvaluatorTest {

    @Test
    public void testEvaluateIfStatement() {
        // สร้าง AST สำหรับ if statement
        MinionStrategyAST.BinaryExpression condition = new MinionStrategyAST.BinaryExpression(
                MinionStrategyAST.BinaryExpression.Operator.GREATER_THAN,
                new MinionStrategyAST.IdentifierExpression("budget"),
                new MinionStrategyAST.NumberExpression(1000)
        );
        MinionStrategyAST.BlockStatement thenStatement = new MinionStrategyAST.BlockStatement(
                List.of(new MinionStrategyAST.AssignmentStatement("x", new MinionStrategyAST.NumberExpression(1)))
        );
        MinionStrategyAST.BlockStatement elseStatement = new MinionStrategyAST.BlockStatement(
                List.of(new MinionStrategyAST.AssignmentStatement("x", new MinionStrategyAST.NumberExpression(2)))
        );
        MinionStrategyAST.IfStatement ifStatement = new MinionStrategyAST.IfStatement(condition, thenStatement, elseStatement);

        // สร้าง GameState และ Evaluator
        GameState gameState = new GameState();
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(gameState);

        // ทดสอบ case ที่ condition เป็นจริง
        gameState.setBudget(1500);
        evaluator.evaluate(ifStatement);
        assertEquals(1, evaluator.getVariables().get("x").longValue());

        // ทดสอบ case ที่ condition เป็นเท็จ
        gameState.setBudget(500);
        evaluator.evaluate(ifStatement);
        assertEquals(2, evaluator.getVariables().get("x").longValue());
    }

    @Test
    public void testEvaluateWhileStatement() {
        // สร้าง AST สำหรับ while statement
        MinionStrategyAST.BinaryExpression condition = new MinionStrategyAST.BinaryExpression(
                MinionStrategyAST.BinaryExpression.Operator.GREATER_THAN,
                new MinionStrategyAST.IdentifierExpression("i"),
                new MinionStrategyAST.NumberExpression(0)
        );
        MinionStrategyAST.BlockStatement statement = new MinionStrategyAST.BlockStatement(
                List.of(
                        new MinionStrategyAST.AssignmentStatement("x",
                                new MinionStrategyAST.BinaryExpression(
                                        MinionStrategyAST.BinaryExpression.Operator.PLUS,
                                        new MinionStrategyAST.IdentifierExpression("x"),
                                        new MinionStrategyAST.NumberExpression(1)
                                )
                        ),
                        new MinionStrategyAST.AssignmentStatement("i",
                                new MinionStrategyAST.BinaryExpression(
                                        MinionStrategyAST.BinaryExpression.Operator.MINUS,
                                        new MinionStrategyAST.IdentifierExpression("i"),
                                        new MinionStrategyAST.NumberExpression(1)
                                )
                        )
                )
        );
        MinionStrategyAST.WhileStatement whileStatement = new MinionStrategyAST.WhileStatement(condition, statement);

        // สร้าง GameState และ Evaluator
        GameState gameState = new GameState();
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(gameState);
        Map<String, Long> variables = new HashMap<>();
        variables.put("x", 0L);
        variables.put("i", 5L);
        evaluator.setVariables(variables);

        // ประเมินผล while statement
        evaluator.evaluate(whileStatement);

        // ตรวจสอบผลลัพธ์
        assertEquals(5, evaluator.getVariables().get("x").longValue());
        assertEquals(0, evaluator.getVariables().get("i").longValue());
    }

    @Test
    public void testEvaluateBlockStatement() {
        // สร้าง AST สำหรับ block statement
        MinionStrategyAST.BlockStatement blockStatement = new MinionStrategyAST.BlockStatement(
                List.of(
                        new MinionStrategyAST.AssignmentStatement("x", new MinionStrategyAST.NumberExpression(1)),
                        new MinionStrategyAST.AssignmentStatement("y", new MinionStrategyAST.NumberExpression(2)),
                        new MinionStrategyAST.AssignmentStatement("z",
                                new MinionStrategyAST.BinaryExpression(
                                        MinionStrategyAST.BinaryExpression.Operator.PLUS,
                                        new MinionStrategyAST.IdentifierExpression("x"),
                                        new MinionStrategyAST.IdentifierExpression("y")
                                )
                        )
                )
        );

        // สร้าง GameState และ Evaluator
        GameState gameState = new GameState();
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(gameState);

        // ประเมินผล block statement
        evaluator.evaluate(blockStatement);

        // ตรวจสอบผลลัพธ์
        assertEquals(1, evaluator.getVariables().get("x").longValue());
        assertEquals(2, evaluator.getVariables().get("y").longValue());
        assertEquals(3, evaluator.getVariables().get("z").longValue());
    }

    @Test
    public void testEvaluateAssignmentStatement() {
        // สร้าง AST สำหรับ assignment statement
        MinionStrategyAST.AssignmentStatement assignmentStatement = new MinionStrategyAST.AssignmentStatement(
                "x", new MinionStrategyAST.NumberExpression(123)
        );

        // สร้าง GameState และ Evaluator
        GameState gameState = new GameState();
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(gameState);

        // ประเมินผล assignment statement
        evaluator.evaluate(assignmentStatement);

        // ตรวจสอบผลลัพธ์
        assertEquals(123, evaluator.getVariables().get("x").longValue());
    }

    @Test
    public void testEvaluateDoneCommand() {
        // สร้าง AST สำหรับ done command
        MinionStrategyAST.DoneCommand doneCommand = new MinionStrategyAST.DoneCommand();

        // สร้าง GameState และ Evaluator
        GameState gameState = new GameState();
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(gameState);

        // ประเมินผล done command
        evaluator.evaluate(doneCommand);

        // ไม่ต้องตรวจสอบผลลัพธ์ เพราะ done command ไม่ได้ทำอะไร
    }

    @Test
    public void testEvaluateMoveCommand() {
        // สร้าง AST สำหรับ move command
        MinionStrategyAST.MoveCommand moveCommand = new MinionStrategyAST.MoveCommand("up");

        // สร้าง GameState และ Evaluator
        GameState gameState = new GameState();
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(gameState);

        // ประเมินผล move command
        evaluator.evaluate(moveCommand);

        // ตรวจสอบผลลัพธ์ (อาจต้อง mock หรือ stub method move() ใน GameState)
        // verify(gameState).move("up");
    }

    @Test
    public void testEvaluateShootCommand() {
        // สร้าง AST สำหรับ shoot command
        MinionStrategyAST.ShootCommand shootCommand = new MinionStrategyAST.ShootCommand(
                "down", new MinionStrategyAST.NumberExpression(500)
        );

        // สร้าง GameState และ Evaluator
        GameState gameState = new GameState();
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(gameState);

        // ประเมินผล shoot command
        evaluator.evaluate(shootCommand);

        // ตรวจสอบผลลัพธ์ (อาจต้อง mock หรือ stub method shoot() ใน GameState)
        // verify(gameState).shoot("down", 500);
    }

    @Test
    public void testEvaluateBinaryExpression() {
        // สร้าง GameState และ Evaluator
        GameState gameState = new GameState();
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(gameState);

        // ทดสอบ operator ต่างๆ
        MinionStrategyAST.BinaryExpression expression1 = new MinionStrategyAST.BinaryExpression(
                MinionStrategyAST.BinaryExpression.Operator.PLUS,
                new MinionStrategyAST.NumberExpression(1),
                new MinionStrategyAST.NumberExpression(2)
        );
        assertEquals(3, evaluator.evaluateExpression(expression1));

        MinionStrategyAST.BinaryExpression expression2 = new MinionStrategyAST.BinaryExpression(
                MinionStrategyAST.BinaryExpression.Operator.MINUS,
                new MinionStrategyAST.NumberExpression(5),
                new MinionStrategyAST.NumberExpression(3)
        );
        assertEquals(2, evaluator.evaluateExpression(expression2));

        //... ทดสอบ operator อื่นๆ
    }

    @Test
    public void testEvaluateNumberExpression() {
        // สร้าง GameState และ Evaluator
        GameState gameState = new GameState();
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(gameState);

        // ทดสอบ NumberExpression
        MinionStrategyAST.NumberExpression numberExpression = new MinionStrategyAST.NumberExpression(123);
        assertEquals(123, evaluator.evaluateExpression(numberExpression));
    }

    @Test
    public void testEvaluateIdentifierExpression() {
        // สร้าง GameState และ Evaluator
        GameState gameState = new GameState();
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(gameState);
        Map<String, Long> variables = new HashMap<>();
        variables.put("x", 123L);
        evaluator.setVariables(variables);

        // ทดสอบ IdentifierExpression
        MinionStrategyAST.IdentifierExpression identifierExpression = new MinionStrategyAST.IdentifierExpression("x");
        assertEquals(123, evaluator.evaluateExpression(identifierExpression));

        // ทดสอบ IdentifierExpression สำหรับตัวแปรพิเศษ
        MinionStrategyAST.IdentifierExpression budgetExpression = new MinionStrategyAST.IdentifierExpression("budget");
        assertEquals(gameState.getBudget(), evaluator.evaluateExpression(budgetExpression));
    }

    @Test
    public void testEvaluateInfoExpression() {
        // สร้าง GameState และ Evaluator
        GameState gameState = new GameState();
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(gameState);

        // ทดสอบ InfoExpression (อาจต้อง mock หรือ stub method ใน GameState)
        MinionStrategyAST.InfoExpression allyExpression = new MinionStrategyAST.InfoExpression(MinionStrategyToken.Type.ALLY);
        // when(gameState.getAlly()).thenReturn(1L);
        assertEquals(gameState.getAlly(), evaluator.evaluateExpression(allyExpression));

        MinionStrategyAST.InfoExpression opponentExpression = new MinionStrategyAST.InfoExpression(MinionStrategyToken.Type.OPPONENT);
        // when(gameState.getOpponent()).thenReturn(2L);
        assertEquals(gameState.getOpponent(), evaluator.evaluateExpression(opponentExpression));
    }

    @Test
    public void testEvaluateNearbyExpression() {
        // สร้าง GameState และ Evaluator
        GameState gameState = new GameState();
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(gameState);

        // ทดสอบ NearbyExpression (อาจต้อง mock หรือ stub method ใน GameState)
        MinionStrategyAST.NearbyExpression nearbyExpression = new MinionStrategyAST.NearbyExpression("up");
        // when(gameState.getNearby("up")).thenReturn(123L);
        assertEquals(gameState.getNearby("up"), evaluator.evaluateExpression(nearbyExpression));
    }
}