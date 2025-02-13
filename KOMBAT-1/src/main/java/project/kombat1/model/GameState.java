package project.kombat1.model;

import project.kombat1.config.Config;
import project.kombat1.minion.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GameState {
    private HexGrid hexGrid;
    private Player player1;
    private Player player2;
    private int currentTurn;
    private int maxTurns;
    private Map<String, Long> variables; // Keep this in GameState
    private Player currentPlayer;
    private Minion.MinionType[] minionTypes = Minion.MinionType.values();

    public GameState() {
        this.hexGrid = new HexGrid();
        this.player1 = new Player("Player 1", Config.INIT_BUDGET);
        this.player2 = new Player("Player 2", Config.INIT_BUDGET);
        this.currentTurn = 1;
        this.maxTurns = Config.MAX_TURNS;

    }

    // Getters
    public HexGrid getHexGrid() { return hexGrid; }
    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public int getCurrentTurn() { return currentTurn; }
    public int getMaxTurns() { return maxTurns; }
    public Map<String, Long> getVariables() { return variables; }
    public void setVariables(Map<String, Long> variables) { this.variables = variables; }
    public Player getCurrentPlayer() { return currentPlayer; }

    public void incrementCurrentTurn() {
        this.currentTurn++;
    }

    // Utility method (could be moved to HexGrid)
    public long getEncodedHexLocation(Hex hex1, Hex hex2) {
        return ((long) hex2.getRow() << 32) | (hex2.getCol() & 0xFFFFFFFFL);
    }
    // Utility method (could be moved to HexGrid)
    public boolean isAdjacent(Hex hex1, Hex hex2) {
        List<Hex> adjacentHexes = hexGrid.getAdjacentHexes(hex1.getRow(), hex1.getCol());
        return adjacentHexes.contains(hex2);
    }
    // Utility method (could be moved to HexGrid)
    public int getHexDistance(Hex hex1, Hex hex2) {
        int dx = Math.abs(hex1.getCol() - hex2.getCol());
        int dy = Math.abs(hex1.getRow() - hex2.getRow());
        int dz = Math.max(0, (dx - dy) / 2);
        return dx + dy + dz;
    }

    // หา Hex เป้าหมายตามทิศทาง (Utility Method - ควรอยู่กับ HexGrid)
    public Hex getTargetHex(Hex currentHex, String direction) {
        int row = currentHex.getRow();
        int col = currentHex.getCol();
        switch (direction.toLowerCase()) {
            case "up":
                return hexGrid.getHex(row - 1, col);
            case "down":
                return hexGrid.getHex(row + 1, col);
            case "up_right":
                return hexGrid.getHex(row - 1, col + 1);
            case "down_right":
                return hexGrid.getHex(row, col + 1);
            case "up_left":
                return hexGrid.getHex(row , col-1);
            case "down_left":
                return hexGrid.getHex(row + 1, col - 1);
            default:
                return null;
        }
    }

    public boolean buyHex(int row, int col, Player player) {
        if (player.getBudget() >= Config.HEX_PURCHASE_COST &&
                hexGrid.isHexEmpty(row, col) &&
                !hexGrid.getAdjacentHexesOwnedByPlayer(row, col, player).isEmpty()) {

            player.setBudget(player.getBudget() - Config.HEX_PURCHASE_COST);
            Hex hex = hexGrid.getHex(row, col);
            hex.setOwner(player); // Set owner of the hex
            return true;
        }
        return false;
    }


    private void endGame() {
        if (currentTurn >= maxTurns) {
            System.out.println("จบเกม! ครบจำนวนเทิร์นสูงสุดแล้ว");

        } else {
            boolean player1HasMinions = false;
            boolean player2HasMinions = false;

            for (int row = 0; row < hexGrid.getNumRows(); row++) {
                for (int col = 0; col < hexGrid.getNumCols(); col++) {
                    Hex hex = hexGrid.getHex(row, col);
                    if (hex != null && hex.getMinion() != null) {
                        if (hex.getMinion().getOwner() == player1) {
                            player1HasMinions = true;
                        }
                        if (hex.getMinion().getOwner() == player2) {
                            player2HasMinions = true;
                        }
                    }
                }
            }
            Player winner = player1HasMinions ? player1 : player2;
            System.out.println("จบเกม! " + winner.getName() + " ชนะ!");
        }
    }

    public void startNewTurn() {
        currentTurn++;
        player1.setBudget(player1.getBudget() + Config.TURN_BUDGET);
        player2.setBudget(player2.getBudget() + Config.TURN_BUDGET);
        if (isGameOver()) {
            endGame();
        }
    }

    private void promptPlayerForPurchase() {
        System.out.println(currentPlayer.getName() + " - งบประมาณ: " + currentPlayer.getBudget());
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nคุณต้องการซื้ออะไร?");
            System.out.println("1. มิเนียน");
            System.out.println("2. Hex");
            System.out.println("3. ข้าม");
            System.out.print("เลือก: ");

            while (!scanner.hasNextInt()) {
                System.out.println("กรุณาใส่ตัวเลข");
                System.out.print("เลือก: ");
                scanner.next();
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    buyMinionFromInput(scanner);
                    break;
                case 2:
                    buyHexFromInput(scanner);
                    break;
                case 3:
                    System.out.println("ข้ามการซื้อ");
                    break;
                default:
                    System.out.println("ตัวเลือกไม่ถูกต้อง");
            }
        } while (choice != 3);
    }

    private void buyMinionFromInput(Scanner scanner) {
        System.out.print("เลือกประเภทมิเนียน (TYPE1, TYPE2, TYPE3): ");
        String minionTypeStr = scanner.nextLine().toUpperCase();System.out.print("ใส่แถว: ");
        int row = scanner.nextInt();
        System.out.print("ใส่คอลัมน์: ");
        int col = scanner.nextInt();
        scanner.nextLine();

        try {
            Minion.MinionType type = Minion.MinionType.valueOf(minionTypeStr);
            if (row < 0 || row >= hexGrid.getNumRows() || col < 0 || col >= hexGrid.getNumCols()) {
                System.out.println("แถวหรือคอลัมน์ไม่ถูกต้อง");
                return;
            }

            if (currentPlayer.buyMinion(type, row, col, hexGrid)) {
                System.out.println("ซื้อ Minion สำเร็จ");
            } else {
                System.out.println("ซื้อ Minion ไม่สำเร็จ");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("ประเภท Minion ไม่ถูกต้อง");
        }
    }

    private void buyHexFromInput(Scanner scanner) {
        System.out.print("ใส่แถว: ");
        int row = scanner.nextInt();
        System.out.print("ใส่คอลัมน์: ");
        int col = scanner.nextInt();
        scanner.nextLine();

        if (row < 0 || row >= hexGrid.getNumRows() || col < 0 || col >= hexGrid.getNumCols()) {
            System.out.println("แถวหรือคอลัมน์ไม่ถูกต้อง");
            return;
        }

        if (buyHex(row, col, currentPlayer)) {
            System.out.println("ซื้อ Hex สำเร็จ");
        } else {
            System.out.println("ซื้อ Hex ไม่สำเร็จ");
        }
    }

    public boolean isGameOver() {
        if (currentTurn >= maxTurns) {
            return true;
        }

        boolean player1HasMinions = false;
        boolean player2HasMinions = false;
        for (int row = 0; row < hexGrid.getNumRows(); row++) {
            for (int col = 0; col < hexGrid.getNumCols(); col++) {
                Hex hex = hexGrid.getHex(row, col);
                if (hex != null && hex.getMinion() != null) {
                    if (hex.getMinion().getOwner() == player1) {
                        player1HasMinions = true;
                    }
                    if (hex.getMinion().getOwner() == player2) {
                        player2HasMinions = true;
                    }
                }
            }
        }
        return !player1HasMinions || !player2HasMinions;
    }

    public void startGame() {
        int mode = getModeFromInput();

        // Remove minion type, name, defense, and strategy setup from here.
        // These are now defined *statically* in the MinionType enum.

        for (Player player : Arrays.asList(player1, player2)) {
            currentPlayer = player;
            Minion.MinionType minionType = getInitialMinionPlacementFromInput(player, minionTypes);
            int row, col;
            do {
                System.out.println(player.getName() + ", เลือก hex ที่จะวาง minion เริ่มต้น:");
                row = getHexCoordinatesFromInput("แถว");
                col = getHexCoordinatesFromInput("คอลัมน์");
            } while (!hexGrid.isHexEmpty(row, col));

            player.buyMinion(minionType, row, col, hexGrid);
        }

        currentPlayer = player1;

        while (!isGameOver()) {
            if (mode == 1 || (mode == 2 && currentPlayer == player1)) {
                promptPlayerForPurchase();
                currentPlayer.promptPlayerTurn(hexGrid, this);
                currentPlayer.executeMinionStrategies(hexGrid, this);
            } else if (mode == 2 && currentPlayer == player2 || mode == 3) {
                executeBotTurn();
            }
            startNewTurn();
            currentPlayer = (currentPlayer == player1) ? player2 : player1;
        }

        endGame();
    }


    private int getNumMinionTypesFromInput() { // Keep this, but it's not used for setting values
        Scanner scanner = new Scanner(System.in);
        int numMinionTypes;
        do {
            System.out.print("ใส่จำนวนประเภท Minion (1-3): "); //  This is now just for show
            while (!scanner.hasNextInt()) {
                System.out.println("กรุณาใส่ตัวเลขระหว่าง 1 ถึง 3");
                System.out.print("ใส่จำนวนประเภท Minion (1-3): ");
                scanner.next();
            }
            numMinionTypes = scanner.nextInt();
        } while (numMinionTypes < 1 || numMinionTypes > 3);
        return numMinionTypes; // We still return it, in case you want to use it *later*
    }

    private String getMinionNameFromInput(int i) {  // No longer used, but kept for compatibility
        Scanner scanner = new Scanner(System.in);
        System.out.print("ใส่ชื่อสำหรับ Minion ประเภท " + (i + 1) + ": ");
        return scanner.nextLine();
    }
    private int getMinionDefenseFromInput(int i) { // No longer used, but kept for compatibility
        Scanner scanner = new Scanner(System.in);
        int defense;
        do {
            System.out.print("ใส่ค่า Defense สำหรับ Minion ประเภท " + (i + 1) + " (200-400): ");
            while (!scanner.hasNextInt()) {
                System.out.println("กรุณาใส่ตัวเลขระหว่าง 200 ถึง 400");
                System.out.print("ใส่ค่า Defense สำหรับ Minion ประเภท " + (i + 1) + " (200-400): ");
                scanner.next();
            }
            defense = scanner.nextInt();
        } while (defense < 200 || defense > 400);
        return defense;
    }

    private String getStrategyFromInput(int i) { // No longer used, but kept for compatibility
        Scanner scanner = new Scanner(System.in);
        System.out.print("ใส่ Strategy สำหรับ Minion ประเภท " + (i + 1) + ": ");
        return scanner.nextLine();
    }


    private Minion.MinionType getInitialMinionPlacementFromInput(Player player, Minion.MinionType[] minionTypes) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println(player.getName() + ", เลือก Minion ประเภทเริ่มต้น:");
            for (int i = 0; i < minionTypes.length; i++) {
                System.out.println((i + 1) + ". " + minionTypes[i] + " (" + minionTypes[i].getName() + ")"); // Display the name
            }
            System.out.print("เลือก: ");
            while (!scanner.hasNextInt()) {
                System.out.println("กรุณาใส่ตัวเลข");
                System.out.print("เลือก: ");
                scanner.next();
            }
            choice = scanner.nextInt();
        } while (choice < 1 || choice > minionTypes.length);
        return minionTypes[choice - 1];
    }

    private int getHexCoordinatesFromInput(String coordinate) {
        Scanner scanner = new Scanner(System.in);
        int value;
        do {
            System.out.print("ใส่ " + coordinate + " (0-7): ");
            while (!scanner.hasNextInt()) {
                System.out.println("กรุณาใส่ตัวเลขระหว่าง 0 ถึง 7");
                System.out.print("ใส่ " + coordinate + " (0-7): ");
                scanner.next();
            }
            value = scanner.nextInt();
        } while (value < 0 || value > 7);
        return value;
    }

    private void executeBotTurn() {
        System.out.println("บอทกำลังดำเนินการ...");
        // ซื้อ Minion ถ้า budget พอ และยังซื้อได้
        if (player2.getMinions().size() < Config.MAX_SPAWNS) {
            for (Minion.MinionType type : Minion.MinionType.values()) {
                if (player2.getBudget() >= type.getCost()) {
                    for (int row = 0; row < hexGrid.getNumRows(); row++) {
                        for (int col = 0; col < hexGrid.getNumCols(); col++) {
                            if (player2.buyMinion(type, row, col, hexGrid)) {
                                System.out.println("บอทซื้อ Minion ประเภท: " + type);
                                break;
                            }
                        }
                        if (player2.getMinions().size() >= Config.MAX_SPAWNS) break;
                    }
                }
                if (player2.getMinions().size() >= Config.MAX_SPAWNS) break;
            }
        }
        // สั่ง Minion โจมตี, เคลื่อนที่ (ตัวอย่างง่ายๆ)
        player2.executeMinionStrategies(hexGrid, this);
    }

    private int getModeFromInput() {
        Scanner scanner = new Scanner(System.in);
        int mode;
        do {
            System.out.println("เลือกโหมดเกม:");
            System.out.println("1. ผู้เล่น vs ผู้เล่น");
            System.out.println("2. ผู้เล่น vs บอท");
            System.out.println("3. บอท vs บอท");
            System.out.print("เลือก: ");
            while (!scanner.hasNextInt()) {
                System.out.println("กรุณาใส่ตัวเลข.");
                System.out.print("Enter your choice: ");
                scanner.next();
            }
            mode = scanner.nextInt();
        } while (mode < 1 || mode > 3);
        return mode;
    }
}