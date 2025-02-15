package kombat.project1_1.model;
import kombat.project1_1.player.*;

import kombat.project1_1.service.GameService;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    protected MinionStrategy strategy;
    @Getter
    @Setter
    protected Player owner;
    private GameService gameService;
    public GameService getGameService() { // getter method
        return gameService;
    }

    public Minion(String name, int cost, int attackPower, int defensePower, int maxHealth, int attackCost, int defenseCost, GameService gameService, MinionStrategy strategy) {
        this.name = name;
        this.cost = cost;
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.attackCost = attackCost;
        this.defenseCost = defenseCost;
        this.strategy = strategy;
        this.owner = null; // กำหนดค่าเริ่มต้น

        this.gameService = this.gameService;
        this.strategy = new PandyMinionStrategy(this.gameService);// ส่ง gameService ไปยัง strategy
        this.strategy = new CheesebearMinionStrategy(this.gameService);
        this.strategy = new MoonumMinionStrategy(this.gameService);
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
        if (owner != null && owner.getCurrentMoney() >= defenseCost) {
            owner.setCurrentMoney(owner.getCurrentMoney() - defenseCost);
            damage = Math.max(0, damage - getDefensePower());
        }
        this.currentHealth -= damage;
        if (this.currentHealth < 0) this.currentHealth = 0; // ป้องกัน HP ติดลบ
    }


    // ตรวจสอบว่ายังมีชีวิตอยู่หรือไม่
    public boolean isAlive() {
        return currentHealth > 0;
    }

    // ทำตามกลยุทธ์ของมินเนียน
    public void executeStrategy(GameState gameState, Player owner) {
        List<Node> commands = (List<Node>) StrategyParser.parse(this.strategy.getStrategyCode()); // 🔥 เรียก getStrategyCode()
        StrategyEvaluator evaluator = new StrategyEvaluator(this, gameState, owner);
        // สร้าง Node จาก List<String> (คุณต้องตรวจสอบว่า Node รองรับหรือไม่)
        Node node = new StatementListNode(commands);

        // เรียกใช้ execute โดยส่ง Node
        evaluator.execute(node);
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

//    public int getDefenseProtection() {
//        return defenseProtection;
//    }

    public int getCostToMove() {
        if (this instanceof Pandy) {
            return 300;
        } else if (this instanceof Cheesebear) {
            return 200;
        } else if (this instanceof Moonum) {
            return 100;
        } else {
            return 0; // หรือค่าเริ่มต้นอื่นๆ
        }
    }
}