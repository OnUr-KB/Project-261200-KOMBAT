package project.kombat1.model;

import lombok.Getter;
import lombok.Setter;
import project.kombat1.config.Config;
import project.kombat1.minion.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Getter
@Setter
public class Minion {

    private String name;
    private int attack;
    private String strategy; // เก็บ path ไปยังไฟล์ strategy
    private Hex hex;
    private Player owner;
    private MinionType type;
    private int hp;
    private boolean current;

    public Minion(String name, int attack, String strategyPath, Hex hex, Player owner, MinionType type) {
        this.name = name;
        this.attack = attack;
        this.strategy = strategyPath; // เก็บเป็น path
        this.hex = hex;
        this.owner = owner;
        this.type = type;
        this.hp = type.getMaxHp(); // ใช้ค่า HP ตาม MinionType
        this.current = false; // ตอนเริ่มต้น Minion ยังไม่ถูกเลือก
    }

    // Getters & Setters
    public Hex getHex() { return hex; }
    public Player getOwner() { return owner; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public void setHex(Hex newHex){ this.hex = newHex; }
    public String getStrategy() { return strategy; } // ควร return เป็น path
    public MinionType getType() { return type; }
    public int getDefense() { return type.getDefense(); } // defense ถูกย้ายไปที่ enum
    public boolean isCurrent() { return current; }
    public void setCurrent(boolean current) { this.current = current; }

    // ✅ Enum สำหรับ MinionType (แยกข้อมูลออกจาก Minion)
    public enum MinionType {
        TYPE1("Warrior", 300, 5000, "path/to/strategy1.txt"),
        TYPE2("Archer", 250, 3000, "path/to/strategy2.txt"),
        TYPE3("Tank", 400, 7000, "path/to/strategy3.txt");

        @Getter private final String name;
        @Getter private final int defense;
        @Getter private final int maxHp;
        @Getter private final String strategyPath;

        MinionType(String name, int defense, int maxHp, String strategyPath) {
            this.name = name;
            this.defense = defense;
            this.maxHp = maxHp;
            this.strategyPath = strategyPath;
        }

        public int getCost() {
            return switch (this) {
                case TYPE1 -> Config.MINION_STRATEGY1_PURCHASE_COST;
                case TYPE2 -> Config.MINION_STRATEGY2_PURCHASE_COST;
                case TYPE3 -> Config.MINION_STRATEGY3_PURCHASE_COST;
            };
        }
    }

    // ✅ รัน Strategy (อ่านไฟล์, แปลง, ประเมิน)
    public void executeStrategy(HexGrid hexGrid, GameState gameState) throws IOException {
        String strategyContent = new String(Files.readAllBytes(Paths.get(this.type.getStrategyPath())));
        List<MinionStrategyToken> tokens = new MinionStrategyLexer(strategyContent).lex();
        MinionStrategyAST.Statement ast = new MinionStrategyParser(tokens).parse();

        // ✅ ใช้ constructor ที่ถูกต้อง (ต้องส่ง 3 arguments)
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(gameState, this, hexGrid);
        evaluator.evaluate(ast);
    }

    // ✅ รับค่า HP สูงสุดของ Minion ตามประเภท
    public int getMaxHp() {
        return this.type.getMaxHp();
    }

    // ✅ ตรวจสอบว่า Minion ตายหรือยัง
    public boolean isAlive() {
        return this.hp > 0;
    }

    // ✅ ฟังก์ชันลด HP เมื่อโดนโจมตี
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) this.hp = 0;
    }

    // ✅ ตรวจสอบว่าสามารถเคลื่อนที่ได้ไหม
    public boolean canMove() {
        return true;
    }

    // ✅ ตรวจสอบว่าสามารถโจมตีได้ไหม
    public boolean canAttack() {
        return true;
    }

    // ✅ รีเซ็ตสถานะ Minion หลังจบเทิร์น
    public void reset() {
        this.current = false;
    }
}
