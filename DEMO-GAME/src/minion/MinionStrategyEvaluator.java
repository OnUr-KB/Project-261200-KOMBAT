package minion;
import minion.MinionStrategyAST;
import model.GameState;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MinionStrategyEvaluator {

    private  Map<String, Long> variables;
    private final GameState gameState;
    private final Random random;


    public Map<String, Long> getVariables() {  // เพิ่ม method นี้
        return variables;
    }

    public void setVariables(Map<String, Long> variables) {  // เพิ่ม method นี้
        this.variables = variables;
    }

    public MinionStrategyEvaluator(GameState gameState) {
        this.variables = new HashMap<>();
        this.gameState = gameState;
        this.random = new Random();
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
        for (MinionStrategyAST.Statement stmt: statement.statements) {
            evaluate(stmt);
        }
    }

    public void evaluateAssignmentStatement(MinionStrategyAST.AssignmentStatement statement) {
        if (!statement.identifier.startsWith("$")) {
            variables.put(statement.identifier, evaluateExpression(statement.expression));
            System.out.println("Variable " + statement.identifier + " = " + variables); // Print ค่าตัวแปร
        }
    }

    public void evaluateActionCommand(MinionStrategyAST.ActionCommand statement) {
        if (statement instanceof MinionStrategyAST.DoneCommand) {
            // Do nothing
        } else if (statement instanceof MinionStrategyAST.MoveCommand) {
            String direction = ((MinionStrategyAST.MoveCommand) statement).direction;
            gameState.move(direction);
        } else if (statement instanceof MinionStrategyAST.ShootCommand) {
            String direction = ((MinionStrategyAST.ShootCommand) statement).direction;
            long expenditure = evaluateExpression(((MinionStrategyAST.ShootCommand) statement).expenditure);
            gameState.shoot(direction, expenditure);
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
                return gameState.getBudget();
            } else if (identifier.equals("inputdefense")) {
                return gameState.getInputDefense();
            } else if (identifier.equals("random")) {
                return random.nextInt(1000);
            } else {
                return variables.getOrDefault(identifier, 0L);
            }
        } else if (expression instanceof MinionStrategyAST.InfoExpression) {
            MinionStrategyToken.Type type = ((MinionStrategyAST.InfoExpression) expression).type;
            if (type == MinionStrategyToken.Type.ALLY) {
                return gameState.getAlly();
            } else if (type == MinionStrategyToken.Type.OPPONENT) {
                return gameState.getOpponent();
            } else {
                throw new RuntimeException("Unexpected info expression: " + expression);
            }
        } else if (expression instanceof MinionStrategyAST.NearbyExpression) {
            String direction = ((MinionStrategyAST.NearbyExpression) expression).direction;
            return gameState.getNearby(direction);
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
