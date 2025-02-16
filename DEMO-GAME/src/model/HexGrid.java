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
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
            return null; // คืนค่า null ถ้า row หรือ column อยู่นอกขอบเขต
        }
        return grid[row][col]; // คืนค่า hex ที่ตำแหน่ง row และ column ที่ระบุ
    }

    // เมธอดสำหรับหา hex ที่อยู่ติดกัน
    public List<Hex> getAdjacentHexes(int row, int col) {
        List<Hex> adjacentHexes = new ArrayList<>();

        // คำนวณ offset ของ row และ column สำหรับแต่ละ hex ที่อยู่ติดกัน รอบๆ hex ที่กำหนด
        int[][] offsets;

        // Special case: Hexagonal grid offsets depend on whether the row is even or odd
        if(row % 2 == 0){ // Even row
            offsets = new int[][]{
                    {-1, 0},  // บน
                    {-1, 1},  // บนขวา
                    {0, 1},   // ขวา
                    {1, 0},   // ล่าง
                    {1, -1},  // ล่างซ้าย
                    {0, -1}   // ซ้าย
            };
        } else { // Odd row
            offsets = new int[][]{
                    {-1, -1},  // บนซ้าย
                    {-1, 0},  // บน
                    {0, 1},   // ขวา
                    {1, 1},   // ล่างขวา
                    {1, 0},  // ล่าง
                    {0, -1}   // ซ้าย
            };
        }


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
        List<Hex> adjacentHexes = getAdjacentHexes(row, col);
        List<Hex> ownedHexes = new ArrayList<>();
        for (Hex hex : adjacentHexes) {
            if (hex.getOwner() == player) {
                ownedHexes.add(hex);
            }
        }
        return ownedHexes;
    }

    public boolean isHexEmpty(int row, int col) {
        Hex hex = getHex(row, col);
        return hex != null && hex.getMinion() == null && hex.getOwner() == null;
    }

    public int getNumRows(){
        return grid.length;
    }

    public int getNumCols(){
        return grid[0].length;
    }
}