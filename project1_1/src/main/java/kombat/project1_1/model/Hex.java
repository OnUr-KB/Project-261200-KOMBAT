package kombat.project1_1.model;

import lombok.Setter;

import java.util.Objects;

public class Hex {
    private final int x, y;
    @Setter
    private Player owner;
    @Setter
    private Minion minion;

    public Hex(int x, int y) {
        this.x = x;
        this.y = y;
        this.owner = null;
        this.minion = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Hex hex = (Hex) obj;
        return x == hex.x && y == hex.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }


    public boolean isOccupied() { return minion != null; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Player getOwner() { return owner; }

    public Minion getMinion() { return minion; }

    public boolean isAdjacent(Hex other) {
        int dx = Math.abs(this.x - other.getX());
        int dy = Math.abs(this.y - other.getY());
        return (dx <= 1 && dy <= 1) && !(dx == 0 && dy == 0);
    }
}