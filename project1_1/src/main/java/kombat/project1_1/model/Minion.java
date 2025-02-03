package kombat.project1_1.model;
import kombat.project1_1.player.MinionStrategy;
import lombok.Getter;
import lombok.Setter;

public abstract class Minion {
    // Getters
    @Getter
    protected String name;
    @Getter
    protected int cost;
    @Getter
    protected int attackPower;
    protected int defensePower;
    protected int maxHealth;
    protected int currentHealth;
    @Getter
    protected int attackCost;
    protected int defenseCost;
    protected int defenseProtection;
    protected MinionStrategy strategy;
    @Setter
    protected Player owner;

    public Minion(String name, int cost, int attackPower, int defensePower, int maxHealth, int attackCost, int defenseCost, int defenseProtection, MinionStrategy strategy) {
        this.name = name;
        this.cost = cost;
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.attackCost = attackCost;
        this.defenseCost = defenseCost;
        this.defenseProtection = defenseProtection;
        this.strategy = strategy;
        this.owner = null; // กำหนดค่าเริ่มต้น

    }

    // โจมตีเป้าหมาย
    public void attack(Minion target, Player owner) {
        if (owner.getCurrentMoney() >= attackCost) {
            owner.setCurrentMoney(owner.getCurrentMoney() - attackCost);
            target.receiveDamage(attackPower);
        }
    }

    // รับความเสียหายพร้อมป้องกันตัวเองถ้ามีเงินเพียงพอ
    public void receiveDamage(int damage) {
        if (owner.getCurrentMoney() >= defenseCost) {
            owner.setCurrentMoney(owner.getCurrentMoney() - defenseCost);
            damage = Math.max(0, damage - defenseProtection);
        }
        this.currentHealth -= damage;
    }

    // ตรวจสอบว่ายังมีชีวิตอยู่หรือไม่
    public boolean isAlive() {
        return currentHealth > 0;
    }

    // ทำตามกลยุทธ์ของมินเนียน
    public void executeStrategy(GameState gameState, Player owner) {
        strategy.execute(this, gameState, owner);
    }

    public int getAttack() { // เพิ่ม getAttack สำหรับการเข้าถึง attackPower
        return attackPower;
    }

    public int getDefensePower() {
        return defensePower;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getDefenseCost() {
        return defenseCost;
    }

    public int getDefenseProtection() {
        return defenseProtection;
    }
}
