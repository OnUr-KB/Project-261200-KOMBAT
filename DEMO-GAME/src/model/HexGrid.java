package model;

import java.util.ArrayList;
import java.util.List;

public class HexGrid {
    private Hex grid; // 2D array สำหรับเก็บออบเจ็กต์ Hex

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
        if (row < 0 || row >= grid.length || col < 0 || col >= grid.length) {
            return null; // คืนค่า null ถ้า row หรือ column อยู่นอกขอบเขต
        }
        return grid[row][col]; // คืนค่า hex ที่ตำแหน่ง row และ column ที่ระบุ
    }

    // เมธอดสำหรับหา hex ที่อยู่ติดกัน
    public List<Hex> getAdjacentHexes(Hex hex) {
        List<Hex> adjacentHexes = new ArrayList<>();
        int row = hex.getRow();
        int col = hex.getCol();

        // คำนวณ offset ของ row และ column สำหรับแต่ละ hex ที่อยู่ติดกัน
        int offsets = {
                {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}
        };

        // วนลูป offset และเพิ่ม hex ที่อยู่ติดกัน vào list
        for (int offset: offsets) {
            int newRow = row + offset;
            int newCol = col + offset;
            Hex adjacentHex = getHex(newRow, newCol);
            if (adjacentHex!= null) {
                adjacentHexes.add(adjacentHex);
            }
        }

        return adjacentHexes; // คืนค่า list ของ hex ที่อยู่ติดกัน
    }


    // Getters and setters for rows, cols
    //...
}