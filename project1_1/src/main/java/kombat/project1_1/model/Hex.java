package kombat.project1_1.model;

import lombok.Setter;

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