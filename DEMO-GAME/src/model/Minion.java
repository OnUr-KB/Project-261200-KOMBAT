// Minion.java (model package)
package main.model;

import minion.MinionStrategyAST;

public class Minion {
    private int hp; // พลังชีวิตของ Minion
    private int defense; // ค่าป้องกันของ Minion
    private MinionStrategyAST.Statement strategy; // กลยุทธ์ของ Minion
    private Hex hex; // Hex ที่ Minion อยู่
    private Player owner; // เจ้าของ Minion
    private int actionPoints; // Action Points ของ Minion
    public static final int MAX_ACTION_POINTS = 5; // Action Points สูงสุด


    public Minion(int hp, int defense, MinionStrategyAST.Statement strategy, Player owner) {
        this.hp = hp;
        this.defense = defense;
        this.strategy = strategy;
        this.hex = null; // เริ่มต้น Minion ยังไม่ได้อยู่ใน Hex ใด
        this.owner = owner;
        this.actionPoints = MAX_ACTION_POINTS; // เริ่มต้นด้วย Action Points เต็ม
    }

    // Getters and setters
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public MinionStrategyAST.Statement getStrategy() {
        return strategy;
    }

    public void setStrategy(MinionStrategyAST.Statement strategy) {
        this.strategy = strategy;
    }

    public Hex getHex() {
        return hex;
    }

    public void setHex(Hex hex) {
        this.hex = hex;
    }
    public Player getOwner() {
        return owner;
    }
    public void setOwner(Player owner){
        this.owner = owner;
    }

    public int getActionPoints() {
        return actionPoints;
    }

    public void setActionPoints(int actionPoints) {
        this.actionPoints = actionPoints;
    }
    public void resetActionPoints() {
        this.actionPoints = MAX_ACTION_POINTS; // รีเซ็ต Action Points
    }


    // โจมตี Minion อื่น
    public void attack(Minion target, int expenditure) {
        if(actionPoints > 0){ // ตรวจสอบ Action Points
            // ลด HP ของ target ตาม expenditure และ defense
            int damage = Math.max(1, expenditure - target.getDefense()); // Damage อย่างน้อย 1
            target.setHp(Math.max(0, target.getHp() - damage)); // ลด HP, ไม่ต่ำกว่า 0
            actionPoints--; // ลด Action Points
        }

    }

    // เคลื่อนที่ไปยัง Hex ที่อยู่ติดกัน
    public boolean move(String direction, HexGrid hexGrid) {
        if(actionPoints <= 0){ // ตรวจสอบ Action Points ก่อน
            return false;
        }
        // หา Hex ปัจจุบันของ Minion
        Hex currentHex = this.getHex();
        if (currentHex == null) { // ถ้า Minion ยังไม่ได้อยู่ใน Hex ไหนเลย
            return false;
        }

        // หา Hex เป้าหมาย
        Hex targetHex = hexGrid.getAdjacentHexes(currentHex.getRow(), currentHex.getCol()) // หา hex ข้างเคียงทั้งหมดก่อน
                .stream() // ใช้ Stream API
                .filter(h -> { // กรอง (filter)
                    switch (direction) { // ใช้ switch เพื่อหา hex ตามทิศ
                        case "up":
                            return h.getRow() == currentHex.getRow() - 1 && h.getCol() == currentHex.getCol();
                        case "down":
                            return h.getRow() == currentHex.getRow() + 1 && h.getCol() == currentHex.getCol();
                        case "upleft":
                            return h.getRow() == currentHex.getRow() - 1 && h.getCol() == currentHex.getCol() - (currentHex.getRow() % 2 == 0 ? 0 : 1);
                        case "upright":
                            return h.getRow() == currentHex.getRow() - 1 && h.getCol() == currentHex.getCol() + (currentHex.getRow() % 2 == 0 ? 1 : 0);
                        case "downleft":
                            return h.getRow() == currentHex.getRow() + 1 && h.getCol() == currentHex.getCol() - (currentHex.getRow() % 2 == 0 ? 0 : 1);
                        case "downright":
                            return h.getRow() == currentHex.getRow() + 1 && h.getCol() == currentHex.getCol() + (currentHex.getRow() % 2 == 0 ? 1 : 0);
                        default:
                            return false; // กรณี direction ไม่ถูกต้อง
                    }
                })
                .findFirst() // เอา hex แรกที่เจอ (ถ้ามี)
                .orElse(null); // ถ้าไม่มี hex ที่ตรงตามเงื่อนไข (ทิศทางผิด, หรือสุดขอบ map), คืนค่า null



        // ตรวจสอบว่า targetHex ว่างและอยู่ในขอบเขตของ grid
        if (targetHex!= null && targetHex.getMinion() == null) {
            currentHex.setMinion(null); // ออกจาก Hex เดิม
            this.hex = targetHex; // เปลี่ยน Hex ปัจจุบัน
            targetHex.setMinion(this); // เข้าสู่ Hex ใหม่
            actionPoints--; // ลด Action Points
            return true; // เคลื่อนที่สำเร็จ
        } else {
            return false; // เคลื่อนที่ไม่สำเร็จ
        }
    }
    public Minion.MinionType getMinionType() {
        for(Minion.MinionType type : Minion.MinionType.values()){
            if (type.getHp() == this.hp && type.getDefense() == this.defense){ //เทียบ hp, defense
                return type;
            }
        }
        return null; // ไม่เจอ type ที่ตรงกัน
    }


    public enum MinionType {
        // กำหนดประเภท Minion พร้อมคุณสมบัติ
        TYPE1(500, 250, 1000, "minion.MinionStrategy1", "Soldier"), // HP, Defense, Cost, Strategy Path, Name
        TYPE2(400, 400, 1200, "minion.MinionStrategy2", "Tank"),
        TYPE3(300, 550, 1500, "minion.MinionStrategy3", "Sniper");

        private final int hp;
        private final int defense;
        private final int cost;
        private final String strategyPath; // Fully qualified path
        private final String name;


        MinionType(int hp, int defense, int cost, String strategyPath, String name) {
            this.hp = hp;
            this.defense = defense;
            this.cost = cost;
            this.strategyPath = strategyPath;
            this.name = name;

        }

        public int getHp() {
            return hp;
        }
        public int getDefense() {
            return defense;
        }

        public int getCost() {
            return cost;
        }
        public String getStrategyPath() {
            return strategyPath;
        }
        public String getName(){
            return name;
        }

    }
}