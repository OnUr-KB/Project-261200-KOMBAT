package kombat.project1_1.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
public class HexGrid {
    private final int width = 8;
    private final int height = 8;
    @Getter
    private final Hex[][] grid;

    public HexGrid() {
        grid = new Hex[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Hex(x, y);
            }
        }
    }

    public Hex getHex(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[x][y];
        }
        return null;
    }

    public List<Hex> getAdjacentHexes(Hex hex) {
        int x = hex.getX();
        int y = hex.getY();
        List<Hex> adjacent = new ArrayList<>();

        int[][] directions = {{-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, 0}, {1, -1}};
        for (int[] dir : directions) {
            Hex neighbor = getHex(x + dir[0], y + dir[1]);
            if (neighbor != null) {
                adjacent.add(neighbor);
            }
        }
        return adjacent;
    }

    public boolean canMove(Hex from, Hex to) {
        return getAdjacentHexes(from).contains(to) && !to.isOccupied();
    }

    public boolean moveMinion(Hex from, Hex to) {
        if (canMove(from, to)) {
            to.setMinion(from.getMinion());
            from.setMinion(null);
            return true;
        }
        return false;
    }

    public String checkHex(int x, int y) {
        Hex hex = getHex(x, y);
        return (hex != null && hex.getOwner() != null) ? hex.getOwner().getName() : "Unowned";
    }

    public boolean myCoordinateHex(int x, int y, String playerName) {
        return checkHex(x, y).equals(playerName);
    }
}