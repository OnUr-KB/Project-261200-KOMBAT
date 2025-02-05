package project.kombat1.model;

import lombok.Getter;

@Getter
public class Hex {

    private int row;
    private int col;
    private Minion minion; // null if empty
    private Player owner;

    public Hex(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Minion getMinion() {
        return minion;
    }

    public void setMinion(Minion minion) {
        this.minion = minion;
    }

    public void setOwner(Player player) {
        this.owner = player; // สมมติว่า Hex มี field owner
    }

    public Player getOwner() {
        return owner;
    }

}