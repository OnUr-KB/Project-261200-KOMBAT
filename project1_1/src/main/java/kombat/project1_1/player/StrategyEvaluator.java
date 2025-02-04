package kombat.project1_1.player;

import java.util.*;

import kombat.project1_1.model.GameState;
import kombat.project1_1.model.Hex;
import kombat.project1_1.model.Minion;
import kombat.project1_1.model.Player;

public class StrategyEvaluator {

    private final Minion minion;
    private final GameState gameState;
    private final Player owner;
    private final Map<String, Long> variables = new HashMap<>();
    private final Random random = new Random();
    private int whileLoopCounter = 0;

    public StrategyEvaluator(Minion minion, GameState gameState, Player owner) {
        this.minion = minion;
        this.gameState = gameState;
        this.owner = owner;
    }

    public void execute(Node node) {
        if (node instanceof StatementListNode) {
            executeStatementList((StatementListNode) node);
        } else if (node instanceof IfNode) {
            executeIfStatement((IfNode) node);
        } else if (node instanceof WhileNode) {
            executeWhileStatement((WhileNode) node);
        } else if (node instanceof AssignmentNode) {
            executeAssignmentStatement((AssignmentNode) node);
        } else if (node instanceof DoneNode) {
            // Do nothing
        } else if (node instanceof MoveNode) {
            executeMoveCommand((MoveNode) node);
        } else if (node instanceof ShootNode) {
            executeShootCommand((ShootNode) node);
        } else {
            throw new IllegalArgumentException("Invalid node type: " + node.getClass());
        }
    }

    private void executeStatementList(StatementListNode node) {
        for (Node statement: node.statements) {
            execute(statement);
        }
    }

    private void executeIfStatement(IfNode node) {
        if (evaluateExpression(node.condition) > 0) {
            execute(node.thenStatement);
        } else {
            execute(node.elseStatement);
        }
    }

    private void executeWhileStatement(WhileNode node) {
        whileLoopCounter = 0;
        while (evaluateExpression(node.condition) > 0 && whileLoopCounter < 10000) {
            execute(node.statement);
            whileLoopCounter++;
        }
    }

    private void executeAssignmentStatement(AssignmentNode node) {
        variables.put(node.identifier, evaluateExpression(node.expression));
    }

    private void executeMoveCommand(MoveNode node) {
        Hex currentHex = gameState.getHexOfMinion(minion);
        Hex targetHex = getTargetHex(currentHex, node.direction);
        if (targetHex!= null && gameState.getHexGrid().canMove(currentHex, targetHex)) {
            gameState.getHexGrid().moveMinion(currentHex, targetHex);
        }
    }

    private void executeShootCommand(ShootNode node) {
        Hex currentHex = gameState.getHexOfMinion(minion);
        Hex targetHex = getTargetHex(currentHex, node.direction);
        if (targetHex!= null && targetHex.isOccupied()) {
            Minion targetMinion = targetHex.getMinion();
            minion.attack(targetMinion, owner);
        }
    }

    private Hex getTargetHex(Hex currentHex, String direction) {
        switch (direction) {
            case "up":
                return gameState.getHexGrid().getHex(currentHex.getX(), currentHex.getY() - 1);
            case "down":
                return gameState.getHexGrid().getHex(currentHex.getX(), currentHex.getY() + 1);
            case "upleft":
                return gameState.getHexGrid().getHex(currentHex.getX() - 1, currentHex.getY() - 1);
            case "upright":
                return gameState.getHexGrid().getHex(currentHex.getX() + 1, currentHex.getY() - 1);
            case "downleft":
                return gameState.getHexGrid().getHex(currentHex.getX() - 1, currentHex.getY() + 1);
            case "downright":
                return gameState.getHexGrid().getHex(currentHex.getX() + 1, currentHex.getY() + 1);
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    private long evaluateExpression(Node node) {
        if (node instanceof BinaryOperatorNode) {
            return evaluateBinaryOperator((BinaryOperatorNode) node);
        } else if (node instanceof NumberNode) {
            return ((NumberNode) node).value;
        } else if (node instanceof IdentifierNode) {
            return variables.getOrDefault(((IdentifierNode) node).identifier, 0L);
        } else if (node instanceof NearbyNode) {
            return evaluateNearbyFunction((NearbyNode) node);
        } else if (node instanceof AllyNode) {
            return evaluateAllyExpression();
        } else if (node instanceof OpponentNode) {
            return evaluateOpponentExpression();
        } else {
            throw new IllegalArgumentException("Invalid node type: " + node.getClass());
        }
    }

    private long evaluateBinaryOperator(BinaryOperatorNode node) {
        long left = evaluateExpression(node.left);
        long right = evaluateExpression(node.right);
        switch (node.operator) {
            case PLUS:
                return left + right;
            case MINUS:
                return left - right;
            case MULTIPLY:
                return left * right;
            case DIVIDE:
                return right!= 0? left / right: 0;
            case MODULO:
                return right!= 0? left % right: 0;
            case POWER:
                return (long) Math.pow(left, right);
            case ASSIGN:
                throw new UnsupportedOperationException("Assignment operator should not be evaluated directly");
            default:
                throw new IllegalArgumentException("Invalid binary operator: " + node.operator);
        }
    }

    private long evaluateNearbyFunction(NearbyNode node) {
        Hex currentHex = gameState.getHexOfMinion(minion);
        Hex targetHex = getTargetHex(currentHex, node.direction);
        if (targetHex!= null && targetHex.isOccupied()) {
            Minion nearbyMinion = targetHex.getMinion();
            int distance = Math.abs(nearbyMinion.getOwner().getOwnedHexes().indexOf(targetHex)
                    - owner.getOwnedHexes().indexOf(currentHex));
            if (nearbyMinion.getOwner() == owner) {
                return -100 * String.valueOf(nearbyMinion.getMaxHealth()).length()
                        - 10 * String.valueOf(nearbyMinion.getDefensePower()).length() - distance;
            } else {
                return 100 * String.valueOf(nearbyMinion.getMaxHealth()).length()
                        + 10 * String.valueOf(nearbyMinion.getDefensePower()).length() + distance;
            }
        } else {
            return 0;
        }
    }

    private long evaluateAllyExpression() {
        Hex currentHex = gameState.getHexOfMinion(minion);
        Hex nearestAllyHex = findNearestAllyHex(currentHex);
        if (nearestAllyHex!= null) {
            return getRelativeHexValue(currentHex, nearestAllyHex);
        } else {
            return 0;
        }
    }

    private Hex findNearestAllyHex(Hex currentHex) {
        List<Hex> visibleHexes = getVisibleHexes(currentHex);
        Hex nearestAllyHex = null;
        int minDistance = Integer.MAX_VALUE;
        for (Hex hex: visibleHexes) {
            if (hex.isOccupied() && hex.getMinion().getOwner() == owner) {
                int distance = Math.abs(hex.getX() - currentHex.getX())
                        + Math.abs(hex.getY() - currentHex.getY());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestAllyHex = hex;
                }
            }
        }
        return nearestAllyHex;
    }

    private long evaluateOpponentExpression() {
        Hex currentHex = gameState.getHexOfMinion(minion);
        Hex nearestOpponentHex = findNearestOpponentHex(currentHex);
        if (nearestOpponentHex!= null) {
            return getRelativeHexValue(currentHex, nearestOpponentHex);
        } else {
            return 0;
        }
    }

    private Hex findNearestOpponentHex(Hex currentHex) {
        List<Hex> visibleHexes = getVisibleHexes(currentHex);
        Hex nearestOpponentHex = null;
        int minDistance = Integer.MAX_VALUE;
        for (Hex hex: visibleHexes) {
            if (hex.isOccupied() && hex.getMinion().getOwner()!= owner) {
                int distance = Math.abs(hex.getX() - currentHex.getX())
                        + Math.abs(hex.getY() - currentHex.getY());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestOpponentHex = hex;
                }
            }
        }
        return nearestOpponentHex;
    }

    private List<Hex> getVisibleHexes(Hex currentHex) {
        List<Hex> visibleHexes = new ArrayList<>();
        int[][] directions = { { -3, 0 }, { -2, -1 }, { -1, -2 }, { 0, -3 }, { 1, -2 }, { 2, -1 }, { 3, 0 }, { 2, 1 },
                { 1, 2 }, { 0, 3 }, { -1, 2 }, { -2, 1 } };

        for (int[] dir : directions) {
            Hex hex = gameState.getHexGrid().getHex(currentHex.getX() + dir[0], currentHex.getY() + dir[1]);
            if (hex != null) {
                visibleHexes.add(hex);
            }
        }

        return visibleHexes;
    }


    private long getRelativeHexValue(Hex currentHex, Hex targetHex) {
        int dx = targetHex.getX() - currentHex.getX();
        int dy = targetHex.getY() - currentHex.getY();
        if (dx == 0 && dy == -1) {
            return 11;
        } else if (dx == 1 && dy == -1) {
            return 12;
        } else if (dx == 1 && dy == 0) {
            return 13;
        } else if (dx == 0 && dy == 1) {
            return 14;
        } else if (dx == -1 && dy == 1) {
            return 15;
        } else if (dx == -1 && dy == 0) {
            return 16;
        } else {
            throw new IllegalArgumentException("Invalid relative hex position");
        }
    }
}