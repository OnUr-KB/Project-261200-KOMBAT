package kombat.project1_1.model;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    @Setter
    private int currentMoney;
    private List<Hex> ownedHexes;
    private List<Minion> minions;

    private static final int INITIAL_MONEY = 100_000;
    private static final int TURN_INCOME = 900;
    private static final double INTEREST_RATE = 0.05;

    public Player(String name) {
        this.name = name;
        this.currentMoney = INITIAL_MONEY;
        this.ownedHexes = new ArrayList<>();
        this.minions = new ArrayList<>();
    }

    // รับรายได้ประจำเทิร์น
    public void earnTurnIncome() {
        currentMoney += TURN_INCOME;
    }

    // รับดอกเบี้ยจากเงินที่เหลือ
    public void earnInterest(double interestPct) {
        currentMoney += (int) (currentMoney * INTEREST_RATE);
    }

    // เพิ่มเงิน
    public void addMoney(int amount) {
        currentMoney += amount;
    }

    // ซื้อ Hex
    public boolean buyHex(Hex hex, int hexCost) {
        if (hex.getOwner() == null && currentMoney >= hexCost && isAdjacentToOwnedHex(hex)) {
            currentMoney -= hexCost;
            hex.setOwner(this);
            ownedHexes.add(hex);
            return true;
        }
        return false;
    }

    // ซื้อมินเนียน
    public boolean buyMinion(Minion minion, Hex hex) {
        if (ownedHexes.contains(hex) && !hex.isOccupied() && currentMoney >= minion.getCost()) {
            currentMoney -= minion.getCost();
            hex.setMinion(minion);
            minions.add(minion);
            return true;
        }
        return false;
    }

    // โจมตีศัตรู
    public boolean attack(Minion attacker, Minion target) {
        if (currentMoney >= attacker.getAttackCost() && attacker.isAlive() && target.isAlive()) {
            currentMoney -= attacker.getAttackCost();
            target.receiveDamage(attacker.getAttack());
            if (!target.isAlive()) {
                minions.remove(target);
            }
            return true;
        }
        return false;
    }

    // ตรวจสอบว่า Hex ติดกับ Hex ของตัวเองหรือไม่
    private boolean isAdjacentToOwnedHex(Hex hex) {
        for (Hex ownedHex : ownedHexes) {
            if (ownedHex.isAdjacent(hex)) {
                return true;
            }
        }
        return false;
    }

    // ตรวจสอบว่าผู้เล่นยังมีมินเนียนที่มีชีวิตอยู่หรือไม่
    public boolean hasMinionsAlive() {
        return minions.stream().anyMatch(Minion::isAlive);
    }

    // Getters และ Setters
    public String getName() {
        return name;
    }

    public int getCurrentMoney() {
        return currentMoney;
    }

    public List<Hex> getOwnedHexes() {
        return ownedHexes;
    }

    public List<Minion> getMinions() {
        return minions;
    }

    public void executeMinionStrategies(GameState gameState) {
        for (Minion minion : minions) {
            if (minion.isAlive()) {
                minion.executeStrategy(gameState,this);
            }
        }
    }
}
