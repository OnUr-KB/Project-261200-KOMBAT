package kombat.project1_1.model;

import java.util.List;

import kombat.project1_1.config.Config;
import lombok.Getter;

import java.util.ArrayList;

public class GameState {
    private int currentTurn;
    private final int maxTurns;
    @Getter
    private final Player player1;
    @Getter
    private final Player player2;
    @Getter
    private final HexGrid hexGrid;

    public GameState(Player player1, Player player2,HexGrid hexGrid, int maxTurns) {
        this.player1 = player1;
        this.player2 = player2;
        this.hexGrid = new HexGrid();
        this.maxTurns = Config.MAX_TURNS;
        this.currentTurn = 1;
    }

    public void nextTurn() {
        currentTurn++;
        player1.addMoney(Config.TURN_BUDGET);
        player2.addMoney(Config.TURN_BUDGET);

        player1.earnInterest(Config.INTEREST_PCT);
        player2.earnInterest(Config.INTEREST_PCT);

        player1.executeMinionStrategies(this);
        player2.executeMinionStrategies(this);
    }

    public boolean isGameOver() {
        return currentTurn > maxTurns || !player1.hasMinionsAlive() || !player2.hasMinionsAlive();
    }

    public Player determineWinner() {
        if (!player1.hasMinionsAlive()) return player2;
        if (!player2.hasMinionsAlive()) return player1;

        int minionCount1 = player1.getMinions().size();
        int minionCount2 = player2.getMinions().size();

        if (minionCount1 > minionCount2) return player1;
        if (minionCount2 > minionCount1) return player2;

        return null; // Draw
    }

    public Hex getHexOfMinion(Minion minion) {
        for (Hex[] row : hexGrid.getGrid()) {
            for (Hex hex : row) {
                if (hex.getMinion() == minion) {
                    return hex;
                }
            }
        }
        return null;
    }

    public Minion findNearestEnemy(Minion minion) {
        List<Minion> allEnemies = new ArrayList<>();
        allEnemies.addAll(player1.getMinions());
        allEnemies.addAll(player2.getMinions());
        allEnemies.remove(minion);

        Minion nearest = null;
        int minDistance = Integer.MAX_VALUE;
        Hex minionHex = getHexOfMinion(minion);

        for (Minion enemy : allEnemies) {
            Hex enemyHex = getHexOfMinion(enemy);
            if (enemyHex != null) {
                int distance = Math.abs(minionHex.getX() - enemyHex.getX()) + Math.abs(minionHex.getY() - enemyHex.getY());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = enemy;
                }
            }
        }
        return nearest;
    }

    public Hex getNextStepTowards(Hex from, Hex to) {
        List<Hex> adjacentHexes = hexGrid.getAdjacentHexes(from);
        Hex nextStep = null;
        int minDistance = Integer.MAX_VALUE;

        for (Hex hex : adjacentHexes) {
            int distance = Math.abs(hex.getX() - to.getX()) + Math.abs(hex.getY() - to.getY());
            if (distance < minDistance && !hex.isOccupied()) {
                minDistance = distance;
                nextStep = hex;
            }
        }
        return nextStep;
    }

    public int getCurrentTurn() { return currentTurn; }
}
