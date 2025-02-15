package main.model;

import java.util.ArrayList;
import java.util.List;

public class HexGrid {
    private Hex[][] grid; // 2D array สำหรับเก็บออบเจ็กต์ Hex

    public HexGrid(int rows, int cols) {
        this.grid = new Hex[rows][cols];
        // กำหนดค่าเริ่มต้นให้กับแต่ละ hex ใน grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                this.grid[row][col] = new Hex(row, col);
            }
        }
    }

    public Hex getHex(int row, int col) {
        // ตรวจสอบว่า row และ column อยู่ในขอบเขตของ grid หรือไม่
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) { // ตรวจสอบ column index ให้ถูกต้อง
            return null; // คืนค่า null ถ้า row หรือ column อยู่นอกขอบเขต
        }
        return grid[row][col]; // คืนค่า hex ที่ตำแหน่ง row และ column ที่ระบุ
    }

    // เมธอดสำหรับหา hex ที่อยู่ติดกัน
    public List<Hex> getAdjacentHexes(int row, int col) { // รับ row และ col เป็น parameters
        List<Hex> adjacentHexes = new ArrayList<>();

        // คำนวณ offset ของ row และ column สำหรับแต่ละ hex ที่อยู่ติดกัน
        int[][] offsets = { // ใช้ 2D array สำหรับ offsets
                {-1, 0}, {-1, 1}, {0, 1}, {1, 0}, {1, -1}, {0, -1}
        };

        // วนลูป offset และเพิ่ม hex ที่อยู่ติดกัน vào list
        for (int[] offset : offsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];
            Hex adjacentHex = getHex(newRow, newCol);
            if (adjacentHex != null) {
                adjacentHexes.add(adjacentHex);
            }
        }

        return adjacentHexes; // คืนค่า list ของ hex ที่อยู่ติดกัน
    }
    public List<Hex> getAdjacentHexesOwnedByPlayer(int row, int col, Player player) {
        List<Hex> adjacentHexes = getAdjacentHexes(row, col); // หา Hex ข้างเคียงทั้งหมด
        List<Hex> ownedHexes = new ArrayList<>();
        for (Hex hex : adjacentHexes) {
            if (hex.getOwner() == player) { // ตรวจสอบว่าเป็นของ player หรือไม่
                ownedHexes.add(hex);
            }
        }
        return ownedHexes; // คืนค่า Hex ข้างเคียงที่เป็นของ player
    }

    public boolean isHexEmpty(int row, int col) {
        Hex hex = getHex(row, col);
        return hex != null && hex.getMinion() == null && hex.getOwner() == null; // Hex ว่าง: ไม่มี Minion และไม่มีเจ้าของ
    }

    public int getNumRows(){
        return grid.length; // คืนค่าจำนวนแถว
    }

    public int getNumCols(){
        return grid[0].length; // คืนค่าจำนวนคอลัมน์
    }


}