package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name; // ชื่อของผู้เล่น
    private int budget; // งบประมาณของผู้เล่น
    private List<Minion> minions; // ลิสต์ของ Minion ที่ผู้เล่นเป็นเจ้าของ
    private int inputDefense; // ค่า inputDefense ที่ผู้เล่นกำหนด

    public Player(String name) {
        this.name = name;
        this.budget = config.Config.INIT_BUDGET; // กำหนดค่าเริ่มต้นของงบประมาณ
        this.minions = new ArrayList<>();
        this.inputDefense = 0; // เริ่มต้น inputDefense เป็น 0
    }

    // Getters and setters for name, budget, minions, and inputDefense
    //...

    // เมธอดสำหรับซื้อ Minion
    public boolean buyMinion(int minionType) {
        int minionCost = 0;
        int maxHp = 0;
        switch (minionType) {
            case 1:
                minionCost = config.Config.MINION_STRATEGY1_PURCHASE_COST;
                maxHp = config.Config.MAX_HP_STRATEGY1;
                break;
            case 2:
                minionCost = config.Config.MINION_STRATEGY2_PURCHASE_COST;
                maxHp = config.Config.MAX_HP_STRATEGY2;
                break;
            case 3:
                minionCost = config.Config.MINION_STRATEGY3_PURCHASE_COST;
                maxHp = config.Config.MAX_HP_STRATEGY3;
                break;
            default:
                return false; // ซื้อ Minion ไม่สำเร็จ
        }

        if (budget >= minionCost && minions.size() < config.Config.MAX_SPAWNS) {
            budget -= minionCost;
            // สร้าง Minion ใหม่และเพิ่ม vào list
            Minion newMinion = new Minion(maxHp, inputDefense, null); // TODO: กำหนด strategy ให้ Minion
            minions.add(newMinion);
            return true; // ซื้อ Minion สำเร็จ
        } else {
            return false; // ซื้อ Minion ไม่สำเร็จ
        }
    }

    // เมธอดสำหรับซื้อ Hex
    public boolean buyHex(Hex hex) {
        if (budget >= config.Config.HEX_PURCHASE_COST && hex.getOwner() == null) {
            budget -= config.Config.HEX_PURCHASE_COST;
            hex.setOwner(this); // กำหนดให้ผู้เล่นเป็นเจ้าของ Hex
            return true; // ซื้อ Hex สำเร็จ
        } else {
            return false; // ซื้อ Hex ไม่สำเร็จ
        }
    }