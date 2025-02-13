package project.kombat1.model;

import project.kombat1.model.Hex;
import java.util.ArrayList;
import java.util.List;

public class HexGrid {

    private Hex[][] grid;

    public HexGrid() {
        grid = new Hex[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                grid[row][col] = new Hex(row, col);
            }
        }
    }
    public int getNumRows() {
        return grid.length;
    }

    public int getNumCols() {
        return grid[0].length;
    }

    public Hex getHex(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return null;
        }
        return grid[row][col];
    }

    public void setHex(int row, int col, Hex hex) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            grid[row][col] = hex;
        }
    }

    public boolean isHexEmpty(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return false;
        }
        return grid[row][col].getMinion() == null;
    }

    public List<Hex> getAdjacentHexes(int row, int col) {
        List<Hex> adjacentHexes = new ArrayList<>();
        // up
        adjacentHexes.add(getHex(row - 1, col));
        // down
        adjacentHexes.add(getHex(row + 1, col));
        // upleft
        if (row % 2 == 0) {
            adjacentHexes.add(getHex(row - 1, col - 1));
        } else {
            adjacentHexes.add(getHex(row - 1, col));
        }
        // upright
        if (row % 2 == 0) {
            adjacentHexes.add(getHex(row - 1, col));
        } else {
            adjacentHexes.add(getHex(row - 1, col + 1));
        }
        // downleft
        if (row % 2 == 0) {
            adjacentHexes.add(getHex(row + 1, col - 1));
        } else {
            adjacentHexes.add(getHex(row + 1, col));
        }
        // downright
        if (row % 2 == 0) {
            adjacentHexes.add(getHex(row + 1, col));
        } else {
            adjacentHexes.add(getHex(row + 1, col + 1));
        }
        return adjacentHexes;
    }

    public int getTotalMinionCount() {
        int count = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (grid[row][col].getMinion()!= null) {
                    count++;
                }
            }
        }
        return count;
    }

    public List<Hex> getAdjacentHexesOwnedByPlayer(int row, int col, Player player) {
        List<Hex> adjacentHexes = getAdjacentHexes(row, col);
        List<Hex> ownedHexes = new ArrayList<>();
        for (Hex hex : adjacentHexes) {
            if (hex != null && hex.getOwner() == player) { // ตรวจสอบว่า hex นั้น player เป็นเจ้าของหรือไม่
                ownedHexes.add(hex);
            }
        }
        return ownedHexes;
    }
}