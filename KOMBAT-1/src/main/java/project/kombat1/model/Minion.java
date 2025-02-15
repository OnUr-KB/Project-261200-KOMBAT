package project.kombat1.model;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Minion {

    private String name;
    private int attack;
    private int defense;
    private String strategy;
    private Hex hex;
    private Player owner;
    private MinionType type;
    private Hex Hex;
    private int hp;

    public Minion(String name, int attack, int defense, String strategy, Hex hex, Player owner, MinionType type) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.strategy = strategy;
        this.hex = hex;
        this.owner = owner;
        this.type = type;
    }

    public Hex getHex() {  // เพิ่ม method นี้
        return hex;
    }

    public void setHex(Hex hex) {  // เพิ่ม method นี้
        this.hex = hex;
    }

    public Player getOwner() {  // เพิ่ม method นี้
        return owner;
    }

    public int getHp() {  // เพิ่ม method นี้
        return hp;
    }

    public void setHp(int hp) {  // เพิ่ม method นี้
        this.hp = hp;
    }


    public enum MinionType {
        TYPE1, TYPE2, TYPE3
    }

    // ใน class Minion
    public String getStrategy() {
        return strategy;
    }

    public MinionType getType() {
        return type;
    }

    private boolean current; // เพิ่ม field current

    public boolean isCurrent() {  // เพิ่ม method นี้
        return current;
    }


    public int getDefense() {
        return defense;
    }


}