package project.kombat1.minion;

import project.kombat1.minion.MinionStrategyAST;
import project.kombat1.model.GameState;
import project.kombat1.model.HexGrid;
import project.kombat1.model.Minion;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MinionStrategyEvaluator {

    private Map<String, Long> variables;
    private final GameState gameState;
    private final Minion minion;  // ✅ เพิ่ม Minion
    private final HexGrid hexGrid; // ✅ เพิ่ม HexGrid
    private final Random random;

    // ✅ Constructor รองรับ Minion และ HexGrid
    public MinionStrategyEvaluator(GameState gameState, Minion minion, HexGrid hexGrid) {
        this.variables = new HashMap<>();
        this.gameState = gameState;
        this.minion = minion;
        this.hexGrid = hexGrid;
        this.random = new Random();
    }

    public Map<String, Long> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Long> variables) {
        this.variables = variables;
    }

    public void evaluate(MinionStrategyAST.Statement statement) {
        if (statement instanceof MinionStrategyAST.IfStatement) {
            evaluateIfStatement((MinionStrategyAST.IfStatement) statement);
        } else if (statement instanceof MinionStrategyAST.WhileStatement) {
            evaluateWhileStatement((MinionStrategyAST.WhileStatement) statement);
        } else if (statement instanceof MinionStrategyAST.BlockStatement) {
            evaluateBlockStatement((MinionStrategyAST.BlockStatement) statement);
        } else if (statement instanceof MinionStrategyAST.AssignmentStatement) {
            evaluateAssignmentStatement((MinionStrategyAST.AssignmentStatement) statement);
        } else if (statement instanceof MinionStrategyAST.ActionCommand) {
            evaluateActionCommand((MinionStrategyAST.ActionCommand) statement);
        } else {
            throw new RuntimeException("Unexpected statement: " + statement);
        }
    }

    public void evaluateIfStatement(MinionStrategyAST.IfStatement statement) {
        if (evaluateExpression(statement.condition) > 0) {
            evaluate(statement.thenStatement);
        } else {
            evaluate(statement.elseStatement);
        }
    }

    public void evaluateWhileStatement(MinionStrategyAST.WhileStatement statement) {
        int counter = 0;
        while (evaluateExpression(statement.condition) > 0 && counter < 10000) {
            evaluate(statement.statement);
            counter++;
        }
    }

    public void evaluateBlockStatement(MinionStrategyAST.BlockStatement statement) {
        for (MinionStrategyAST.Statement stmt : statement.statements) {
            evaluate(stmt);
        }
    }

    public void evaluateAssignmentStatement(MinionStrategyAST.AssignmentStatement statement) {
        if (!statement.identifier.startsWith("$")) {
            variables.put(statement.identifier, evaluateExpression(statement.expression));
            System.out.println("Variable " + statement.identifier + " = " + variables);
        }
    }

    public void evaluateActionCommand(MinionStrategyAST.ActionCommand statement) {
        if (statement instanceof MinionStrategyAST.DoneCommand) {
            // Do nothing
        } else if (statement instanceof MinionStrategyAST.MoveCommand) {
            String direction = ((MinionStrategyAST.MoveCommand) statement).direction;
            minion.getOwner().move(direction, hexGrid, gameState); // ✅ ใช้ `minion.getOwner()` ในการสั่ง move
        } else if (statement instanceof MinionStrategyAST.ShootCommand) {
            String direction = ((MinionStrategyAST.ShootCommand) statement).direction;
            long expenditure = evaluateExpression(((MinionStrategyAST.ShootCommand) statement).expenditure);
            minion.getOwner().shoot(direction, expenditure, hexGrid, gameState); // ✅ ใช้ `minion.getOwner()` ในการสั่ง shoot
        } else {
            throw new RuntimeException("Unexpected action command: " + statement);
        }
    }

    public long evaluateExpression(MinionStrategyAST.Expression expression) {
        if (expression instanceof MinionStrategyAST.BinaryExpression) {
            return evaluateBinaryExpression((MinionStrategyAST.BinaryExpression) expression);
        } else if (expression instanceof MinionStrategyAST.NumberExpression) {
            return ((MinionStrategyAST.NumberExpression) expression).value;
        } else if (expression instanceof MinionStrategyAST.IdentifierExpression) {
            String identifier = ((MinionStrategyAST.IdentifierExpression) expression).identifier;
            if (identifier.equals("budget")) {
                return minion.getOwner().getBudget(); // ✅ เปลี่ยนให้ใช้ `minion.getOwner()`
            } else if (identifier.equals("inputdefense")) {
                return minion.getDefense(); // ✅ เปลี่ยนเป็น `minion.getDefense()`
            } else if (identifier.equals("random")) {
                return random.nextInt(1000);
            } else {
                return variables.getOrDefault(identifier, 0L);
            }
        } else if (expression instanceof MinionStrategyAST.InfoExpression) {
            MinionStrategyToken.Type type = ((MinionStrategyAST.InfoExpression) expression).type;
            if (type == MinionStrategyToken.Type.ALLY) {
                return minion.getOwner().getAlly(hexGrid, gameState); // ✅ ใช้ `getAlly()`
            } else if (type == MinionStrategyToken.Type.OPPONENT) {
                return minion.getOwner().getOpponent(hexGrid, gameState); // ✅ ใช้ `getOpponent()`
            } else {
                throw new RuntimeException("Unexpected info expression: " + expression);
            }
        } else if (expression instanceof MinionStrategyAST.NearbyExpression) {
            String direction = ((MinionStrategyAST.NearbyExpression) expression).direction;
            return minion.getOwner().getNearby(direction, hexGrid, gameState); // ✅ ใช้ `getNearby()`
        } else {
            throw new RuntimeException("Unexpected expression: " + expression);
        }
    }

    public long evaluateBinaryExpression(MinionStrategyAST.BinaryExpression expression) {
        long left = evaluateExpression(expression.left);
        long right = evaluateExpression(expression.right);
        switch (expression.operator) {
            case PLUS:
                return left + right;
            case MINUS:
                return left - right;
            case MULTIPLY:
                return left * right;
            case DIVIDE:
                return left / right;
            case MODULO:
                return left % right;
            case POWER:
                return (long) Math.pow(left, right);
            default:
                throw new RuntimeException("Unexpected binary operator: " + expression.operator);
        }
    }
}
