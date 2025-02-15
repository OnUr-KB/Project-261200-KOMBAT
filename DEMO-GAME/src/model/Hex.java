package model;

public class Hex {
    private int row;
    private int col;
    private Player owner;
    private Minion minion;

    public Hex(int row, int col) {
        this.row = row;
        this.col = col;
        this.owner = null;
        this.minion = null;
    }

    // Getters and setters for row, col, owner, and minion
    //...
}