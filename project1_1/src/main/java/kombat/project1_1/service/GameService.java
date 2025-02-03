package kombat.project1_1.service;
import kombat.project1_1.model.*;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class GameService {
    private GameState gameState;

    public void startGame(Player player1, Player player2, int maxTurns) {
        HexGrid hexGrid = new HexGrid();
        gameState = new GameState(player1, player2, hexGrid, maxTurns);
    }

    // ซื้อ Hex
    public boolean buyHex(Player player, Hex hex, int cost) {
        if (player.getCurrentMoney() >= cost && !hex.isOccupied() && hex.getOwner() == null) {
            player.setCurrentMoney(player.getCurrentMoney() - cost);
            hex.setOwner(player);
            player.getOwnedHexes().add(hex);
            return true;
        }
        return false;
    }

    // วางมินเนียน
    public boolean placeMinion(Player player, Minion minion, Hex hex) {
        if (player.getOwnedHexes().contains(hex) && !hex.isOccupied() && player.getCurrentMoney() >= minion.getCost()) {
            player.setCurrentMoney(player.getCurrentMoney() - minion.getCost());
            hex.setMinion(minion);
            player.getMinions().add(minion);
            return true;
        }
        return false;
    }

    // โจมตีศัตรู
    public boolean attack(Player attacker, Minion attackingMinion, Minion targetMinion) {
        if (attacker.getCurrentMoney() >= attackingMinion.getAttackCost() && attackingMinion.isAlive() && targetMinion.isAlive()) {
            attacker.setCurrentMoney(attacker.getCurrentMoney() - attackingMinion.getAttackCost());
            targetMinion.receiveDamage(attackingMinion.getAttackPower());
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
