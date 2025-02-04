package kombat.project1_1.player;


import kombat.project1_1.model.GameState;
import kombat.project1_1.model.Hex;
import kombat.project1_1.model.Minion;
import kombat.project1_1.model.Player;
import kombat.project1_1.service.GameService;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PandyMinionStrategy implements MinionStrategy {

    private final Random random = new Random();
    private GameService gameService;
    public PandyMinionStrategy(GameService gameService) {
        this.gameService = gameService;
    }


    @Override
    public void execute(Minion minion, GameState gameState, Player owner) {
        Hex currentHex = gameState.getHexOfMinion(minion);

        // เดินสุ่ม 2 Hex ที่ติดกัน
        List<Hex> adjacentHexes = gameState.getHexGrid().getAdjacentHexes(currentHex);
        for (int i = 0; i < 2; i++) {
            if (adjacentHexes.isEmpty()) break; // ถ้าไม่มี hex ที่ติดกัน ให้หยุดการเดิน
            Hex nextHex = adjacentHexes.get(random.nextInt(adjacentHexes.size()));
            if (owner.getCurrentMoney() >= minion.getCostToMove()
                    && gameState.getHexGrid().canMove(currentHex, nextHex)
                    && nextHex.getOwner() == owner
                    &&!nextHex.isOccupied()) {

                owner.setCurrentMoney(owner.getCurrentMoney() - minion.getCostToMove());
                gameState.getHexGrid().moveMinion(currentHex, nextHex);
                currentHex = nextHex;
                adjacentHexes = gameState.getHexGrid().getAdjacentHexes(currentHex); // อัพเดต adjacentHexes
            }
        }

        // ค้นหาศัตรูที่อยู่ติดกัน
        List<Minion> adjacentEnemies = gameState.getHexGrid().getAdjacentHexes(currentHex).stream()
                .map(Hex::getMinion)
                .filter(Objects::nonNull)
                .filter(enemy -> enemy.getOwner()!= owner)
                .toList();

        // โจมตีศัตรูที่อยู่ติดกัน
        if (!adjacentEnemies.isEmpty()) {
            Minion target = adjacentEnemies.get(0); // โจมตีศัตรูตัวแรกที่พบ
            if (owner.getCurrentMoney() >= minion.getAttackCost()) {
                gameService.attack(minion, target);
            }
        }

        // ถ้าไม่เจอศัตรูที่อยู่ติดกัน ให้ค้นหาศัตรูที่ใกล้ที่สุด
        if (adjacentEnemies.isEmpty()) {
            Minion nearestEnemy = gameState.findNearestEnemy(minion);
            if (nearestEnemy!= null) {
                Hex targetHex = gameState.getHexOfMinion(nearestEnemy);

                // เดินไปหาศัตรูที่ใกล้ที่สุด
                while (!currentHex.isAdjacent(targetHex)) {
                    Hex nextHex = gameState.getNextStepTowards(currentHex, targetHex);
                    if (nextHex == null || nextHex.getOwner()!= owner || nextHex.isOccupied() || owner.getCurrentMoney() < minion.getCostToMove()) {
                        break; // หยุดเดินถ้าไม่สามารถเดินไป hex ถัดไปได้
                    }
                    owner.setCurrentMoney(owner.getCurrentMoney() - minion.getCostToMove());
                    gameState.getHexGrid().moveMinion(currentHex, nextHex);
                    currentHex = nextHex;
                }

                // โจมตีศัตรูที่ใกล้ที่สุด (ถ้าอยู่ติดกันแล้ว)
                if (currentHex.isAdjacent(targetHex) && owner.getCurrentMoney() >= minion.getAttackCost()) {
                    gameService.attack(minion, nearestEnemy);
                }
            }
        }
    }
}