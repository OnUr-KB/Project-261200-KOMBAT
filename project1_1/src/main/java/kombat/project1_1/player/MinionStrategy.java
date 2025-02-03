package kombat.project1_1.player;

import kombat.project1_1.model.GameState;
import kombat.project1_1.model.Minion;
import kombat.project1_1.model.Player;
import kombat.project1_1.model.Hex;

import java.util.List;
import java.util.Random;

public interface MinionStrategy {
    void execute(Minion minion, GameState gameState, Player owner);
}

class PandyMinionStrategy implements MinionStrategy {
    private final Random random = new Random();

    public void execute(Minion minion, GameState gameState, Player owner) {
        Hex currentHex = gameState.getHexOfMinion(minion);
        List<Hex> adjacentHexes = gameState.getHexGrid().getAdjacentHexes(currentHex);

        // เดินสุ่ม 2 Hex ที่ติดกัน (300 ต่อ Hex)
        for (int i = 0; i < 2; i++) {
            Hex nextHex = adjacentHexes.get(random.nextInt(adjacentHexes.size()));
            if (owner.getCurrentMoney() >= 300 && gameState.getHexGrid().canMove(currentHex, nextHex)) {
                owner.setCurrentMoney(owner.getCurrentMoney() - 300);
                gameState.getHexGrid().moveMinion(currentHex, nextHex);
                currentHex = nextHex;
            }
        }

        // ค้นหาศัตรูแล้วเดินไปหา
        Minion enemy = gameState.findNearestEnemy(minion);
        if (enemy != null) {
            while (!gameState.getHexGrid().getAdjacentHexes(currentHex).contains(gameState.getHexOfMinion(enemy))) {
                Hex nextHex = gameState.getNextStepTowards(currentHex, gameState.getHexOfMinion(enemy));
                if (owner.getCurrentMoney() >= 300 && gameState.getHexGrid().canMove(currentHex, nextHex)) {
                    owner.setCurrentMoney(owner.getCurrentMoney() - 300);
                    gameState.getHexGrid().moveMinion(currentHex, nextHex);
                    currentHex = nextHex;
                } else {
                    break;
                }
            }
            // โจมตีเมื่ออยู่ติดกัน
            if (gameState.getHexGrid().getAdjacentHexes(currentHex).contains(gameState.getHexOfMinion(enemy))) {
                minion.attack(enemy, owner);
            }
        }
    }
}

class CheesebearMinionStrategy implements MinionStrategy {
    private final Random random = new Random();

    public void execute(Minion minion, GameState gameState, Player owner) {
        Hex currentHex = gameState.getHexOfMinion(minion);
        List<Hex> adjacentHexes = gameState.getHexGrid().getAdjacentHexes(currentHex);

        // เดินสุ่ม 2 Hex (200 ต่อ Hex)
        for (int i = 0; i < 2; i++) {
            Hex nextHex = adjacentHexes.get(random.nextInt(adjacentHexes.size()));
            if (owner.getCurrentMoney() >= 200 && gameState.getHexGrid().canMove(currentHex, nextHex)) {
                owner.setCurrentMoney(owner.getCurrentMoney() - 200);
                gameState.getHexGrid().moveMinion(currentHex, nextHex);
                currentHex = nextHex;
            }
        }

        // ค้นหาศัตรูและโจมตี
        Minion enemy = gameState.findNearestEnemy(minion);
        if (enemy != null) {
            while (!gameState.getHexGrid().getAdjacentHexes(currentHex).contains(gameState.getHexOfMinion(enemy))) {
                Hex nextHex = gameState.getNextStepTowards(currentHex, gameState.getHexOfMinion(enemy));
                if (owner.getCurrentMoney() >= 200 && gameState.getHexGrid().canMove(currentHex, nextHex)) {
                    owner.setCurrentMoney(owner.getCurrentMoney() - 200);
                    gameState.getHexGrid().moveMinion(currentHex, nextHex);
                    currentHex = nextHex;
                } else {
                    break;
                }
            }
            if (gameState.getHexGrid().getAdjacentHexes(currentHex).contains(gameState.getHexOfMinion(enemy))) {
                minion.attack(enemy, owner);
            }
        }
    }
}

class MoonumMinionStrategy implements MinionStrategy {
    private final Random random = new Random();

    public void execute(Minion minion, GameState gameState, Player owner) {
        Hex currentHex = gameState.getHexOfMinion(minion);
        List<Hex> adjacentHexes = gameState.getHexGrid().getAdjacentHexes(currentHex);

        // เดินสุ่ม 3 Hex (100 ต่อ Hex)
        for (int i = 0; i < 3; i++) {
            Hex nextHex = adjacentHexes.get(random.nextInt(adjacentHexes.size()));
            if (owner.getCurrentMoney() >= 100 && gameState.getHexGrid().canMove(currentHex, nextHex)) {
                owner.setCurrentMoney(owner.getCurrentMoney() - 100);
                gameState.getHexGrid().moveMinion(currentHex, nextHex);
                currentHex = nextHex;
            }
        }

        // ค้นหาศัตรูและโจมตี
        Minion enemy = gameState.findNearestEnemy(minion);
        if (enemy != null) {
            while (!gameState.getHexGrid().getAdjacentHexes(currentHex).contains(gameState.getHexOfMinion(enemy))) {
                Hex nextHex = gameState.getNextStepTowards(currentHex, gameState.getHexOfMinion(enemy));
                if (owner.getCurrentMoney() >= 100 && gameState.getHexGrid().canMove(currentHex, nextHex)) {
                    owner.setCurrentMoney(owner.getCurrentMoney() - 100);
                    gameState.getHexGrid().moveMinion(currentHex, nextHex);
                    currentHex = nextHex;
                } else {
                    break;
                }
            }
            if (gameState.getHexGrid().getAdjacentHexes(currentHex).contains(gameState.getHexOfMinion(enemy))) {
                minion.attack(enemy, owner);
            }
        }
    }
}
