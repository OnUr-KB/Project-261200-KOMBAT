// Hex.java (model package)
package main.model;

public class Hex {
    private int row; // แถว
    private int col; // คอลัมน์
    private Player owner; // เจ้าของ Hex (null ถ้าไม่มี)
    private Minion minion; // Minion ที่อยู่ใน Hex (null ถ้าไม่มี)

    public Hex(int row, int col) {
        this.row = row;
        this.col = col;
        this.owner = null;  // เริ่มต้น Hex ไม่มีเจ้าของ
        this.minion = null; // เริ่มต้น Hex ไม่มี Minion
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Minion getMinion() {
        return minion;
    }

    public void setMinion(Minion minion) {
        this.minion = minion;
    }

    // จำเป็นสำหรับการเปรียบเทียบ Hex ใน List, HashMap, ฯลฯ  (สำคัญมาก)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // ถ้าเป็น object เดียวกัน
        if (obj == null || getClass() != obj.getClass()) return false; // ถ้าเป็น null หรือคนละ class
        Hex hex = (Hex) obj; // cast object เป็น Hex
        return row == hex.row && col == hex.col; // เปรียบเทียบ row และ col
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(row, col); // สร้าง hash code จาก row และ col
    }
}