package kombat.project1_1.service;


import kombat.project1_1.player.Pandy;
import kombat.project1_1.model.*;
import kombat.project1_1.config.Config;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Random;

@Getter
@Service
public class GameService {

    private GameState gameState;
    private GameService gameService;

    public void startGame(Player player1, Player player2, int maxTurns) {
        HexGrid hexGrid = new HexGrid();

        // กำหนดตำแหน่งเริ่มต้นของผู้เล่น
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i + j <= 4) {
                    hexGrid.getHex(i, j).setOwner(player1);
                    player1.getOwnedHexes().add(hexGrid.getHex(i, j));
                }
                if (i + j >= 6) {
                    hexGrid.getHex(7 - i, 7 - j).setOwner(player2);
                    player2.getOwnedHexes().add(hexGrid.getHex(7 - i, 7 - j));
                }
            }
        }

        // วางมินเนี่ยนเริ่มต้น (สมมติว่า Pandy คือมินเนี่ยนเริ่มต้น)
        player1.buyMinion(new Pandy(gameService), hexGrid.getHex(0, 0));
        player2.buyMinion(new Pandy(gameService), hexGrid.getHex(7, 7));

        // สุ่มเลือกผู้เล่นเริ่มต้น
        Random random = new Random();
        Player startingPlayer = random.nextBoolean()? player1: player2;

        gameState = new GameState(player1, player2, hexGrid, maxTurns > 0? maxTurns: Config.MAX_TURNS, startingPlayer);
    }

    // ซื้อ Hex
    public boolean buyHex(Player player, Hex hex) {
        if (player.getCurrentMoney() >= Config.HEX_PURCHASE_COST &&!hex.isOccupied() && hex.getOwner() == null && player.isAdjacentToOwnedHex(hex)) {
            player.setCurrentMoney(player.getCurrentMoney() - Config.HEX_PURCHASE_COST);
            hex.setOwner(player);
            player.getOwnedHexes().add(hex);
            return true;
        }
        return false;
    }

    // วางมินเนียน
    public boolean placeMinion(Player player, Minion minion, Hex hex) {
        if (player.getOwnedHexes().contains(hex) &&!hex.isOccupied() && player.getCurrentMoney() >= minion.getCost()) {
            player.setCurrentMoney(player.getCurrentMoney() - minion.getCost());
            hex.setMinion(minion);
            minion.setOwner(player); // ตั้งค่า owner ของ minion
            player.getMinions().add(minion);
            return true;
        }
        return false;
    }

    // โจมตีศัตรู
    public boolean attack(Minion attackingMinion, Minion targetMinion) {
        Player attacker = attackingMinion.getOwner();
        Hex attackingHex = gameState.getHexOfMinion(attackingMinion);
        Hex targetHex = gameState.getHexOfMinion(targetMinion);

        if (attacker.getCurrentMoney() >= attackingMinion.getAttackCost()
                && attackingMinion.isAlive()
                && targetMinion.isAlive()
                && attackingHex.isAdjacent(targetHex)) { // ตรวจสอบว่ามินเนี่ยนอยู่ติดกัน

            attacker.setCurrentMoney(attacker.getCurrentMoney() - attackingMinion.getAttackCost());
            targetMinion.receiveDamage(attackingMinion.getAttackPower());
            if (!targetMinion.isAlive()) {
                targetHex.setMinion(null);
                targetMinion.getOwner().getMinions().remove(targetMinion);
            }
            return true;
        }
        return false;
    }

    // จบเทิร์น
    public void endTurn() {
        gameState.nextTurn();
    }

    // ตรวจสอบว่าเกมจบหรือยัง
    public boolean isGameOver() {
        return gameState.isGameOver();
    }

    // หาผู้ชนะ
    public Player determineWinner() {
        return gameState.determineWinner();
    }
}