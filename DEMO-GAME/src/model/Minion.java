package model;

import minion.MinionStrategyAST;

public class Minion {
    private int hp; // พลังชีวิตของ Minion
    private int defense; // ค่าป้องกันของ Minion
    private MinionStrategyAST.Statement strategy; // กลยุทธ์ของ Minion
    private Hex hex; // Hex ที่ Minion อยู่

    public Minion(int hp, int defense, MinionStrategyAST.Statement strategy) {
        this.hp = hp;
        this.defense = defense;
        this.strategy = strategy;
        this.hex = null; // เริ่มต้น Minion ยังไม่ได้อยู่ใน Hex ใด
    }

    // Getters and setters for hp, defense, strategy, and hex
    //...

    // เมธอดสำหรับโจมตี Minion อื่น
    public void attack(Minion target, int expenditure) {
        // ลด HP ของ target ตาม expenditure และ defense
        int damage = Math.max(1, expenditure - target.defense); // Damage อย่างน้อย 1
        target.hp = Math.max(0, target.hp - damage);
    }

    // เมธอดสำหรับเคลื่อนที่ไปยัง Hex ที่อยู่ติดกัน
    public boolean move(String direction, HexGrid hexGrid) {
        // หา Hex ปัจจุบันของ Minion
        Hex currentHex = this.getHex();

        // หา Hex เป้าหมาย
        Hex targetHex = null;
        switch (direction) {
            case "up":
                targetHex = hexGrid.getHex(currentHex.getRow() - 1, currentHex.getCol());
                break;
            case "down":
                targetHex = hexGrid.getHex(currentHex.getRow() + 1, currentHex.getCol());
                break;
            case "upleft":
                targetHex = hexGrid.getHex(currentHex.getRow() - 1, currentHex.getCol() - 1);
                break;
            case "upright":
                targetHex = hexGrid.getHex(currentHex.getRow() - 1, currentHex.getCol() + 1);
                break;
            case "downleft":
                targetHex = hexGrid.getHex(currentHex.getRow() + 1, currentHex.getCol() - 1);
                break;
            case "downright":
                targetHex = hexGrid.getHex(currentHex.getRow() + 1, currentHex.getCol() + 1);
                break;
        }

        // ตรวจสอบว่า targetHex ว่างและอยู่ในขอบเขตของ grid
        if (targetHex!= null && targetHex.getMinion() == null) {
            currentHex.setMinion(null); // ออกจาก Hex เดิม
            this.hex = targetHex; // เปลี่ยน Hex ปัจจุบัน
            targetHex.setMinion(this); // เข้าสู่ Hex ใหม่
            return true; // เคลื่อนที่สำเร็จ
        } else {
            return false; // เคลื่อนที่ไม่สำเร็จ
        }
    }
}