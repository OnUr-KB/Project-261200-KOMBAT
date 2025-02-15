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
    private long budget;
    private int inputDefense;
    private Map<String, Long> variables;
    private Player currentPlayer;
    private boolean player1HasMinions; // เพิ่มบรรทัดนี้
    private boolean player2HasMinions; // เพิ่มบรรทัดนี้
    private Minion.MinionType[] minionTypes = Minion.MinionType.values(); // แก้ไขบรรทัดนี้


    public GameState() {
        this.hexGrid = new HexGrid();
        this.player1 = new Player("Player 1", Config.INIT_BUDGET);
        this.player2 = new Player("Player 2", Config.INIT_BUDGET);
        this.currentTurn = 1;
        this.maxTurns = Config.MAX_TURNS;
        this.budget = Config.INIT_BUDGET;
        this.inputDefense = 200; // or get this from config if available
        Scanner scanner = new Scanner(System.in); // ประกาศ scanner ใน method นี้
    }

    public HexGrid getHexGrid() {
        return hexGrid;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    private Minion getCurrentMinion() {  // เพิ่ม method นี้
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Hex hex = hexGrid.getHex(row, col);
                Minion minion = hex.getMinion();
                if (minion != null && minion.isCurrent()) { // สมมติว่ามีเมธอด isCurrent() ใน Minion
                    return minion;
                }
            }
        }
        return null; // ไม่มี Minion ปัจจุบัน
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void incrementCurrentTurn() {
        this.currentTurn++;
    }

    public int getMaxTurns() {
        return maxTurns;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getInputDefense() {
        return inputDefense;
    }

    public Map<String, Long> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Long> variables) {
        this.variables = variables;
    }

    public boolean buyMinion(Minion.MinionType type, int row, int col, Player player) {
        int minionCost = getCostOfMinion(type);
        if (player.getBudget() >= minionCost && hexGrid.isHexEmpty(row, col)) {
            Minion minion = new Minion("Minion", 0, 0, getStrategyFileForType(type), hexGrid.getHex(row, col), player, type);
            hexGrid.getHex(row, col).setMinion(minion);
            player.setBudget(player.getBudget() - minionCost);
            return true;
        } else {
            return false;
        }
    }

    private String getStrategyFileForType(Minion.MinionType type) {
        return switch (type) {
            case TYPE1 -> "strategy1";
            case TYPE2 -> "strategy2";
            case TYPE3 -> "strategy3";
        };
    }

    private int getCostOfMinion(Minion.MinionType type) {
        return switch (type) {
            case TYPE1 -> Config.MINION_STRATEGY1_PURCHASE_COST;
            case TYPE2 -> Config.MINION_STRATEGY2_PURCHASE_COST;
            case TYPE3 -> Config.MINION_STRATEGY3_PURCHASE_COST;
        };
    }

// ใน class GameState
    public boolean buyHex(int row, int col, Player player) {
        if (player.getBudget() >= Config.HEX_PURCHASE_COST &&
                hexGrid.isHexEmpty(row, col) &&
                !hexGrid.getAdjacentHexesOwnedByPlayer(row, col, player).isEmpty()) {

            player.setBudget(player.getBudget() - Config.HEX_PURCHASE_COST);

            // อัพเดตเจ้าของ Hex
            Hex hex = hexGrid.getHex(row, col);
            hex.setOwner(player); // สมมติว่า Hex มี method setOwner(Player player)

            return true;
        } else {
            return false;
        }
    }

    public void move(String direction) {
        Minion currentMinion = getCurrentMinion(); // สมมติว่ามี method getCurrentMinion() ใน GameState
        if (currentMinion!= null) {
            Hex targetHex = getTargetHex(currentMinion.getHex(), direction);
            if (targetHex!= null && targetHex.getMinion() == null && budget >= 1) {
                currentMinion.getHex().setMinion(null);
                targetHex.setMinion(currentMinion);
                currentMinion.setHex(targetHex);
                budget--;
            }
        }
        Hex targetHex = getTargetHex(currentMinion.getHex(), direction); // ประกาศ targetHex
        if (targetHex!= null) {
            // เพิ่มการตรวจสอบตำแหน่งเป้าหมาย
            if (targetHex.getOwner()!= currentMinion.getOwner() || targetHex.getMinion()!= null) {
                System.out.println("Invalid target hex.");
                return;
            }
        }
    }

    public void shoot(String direction, long expenditure) {
        Minion currentMinion = getCurrentMinion();
        if (currentMinion!= null) {
            Hex targetHex = getTargetHex(currentMinion.getHex(), direction);
            if (targetHex!= null && budget >= expenditure + 1) {
                budget -= expenditure + 1;
                Minion targetMinion = targetHex.getMinion();
                if (targetMinion!= null) {
                    // ตรวจสอบว่า targetMinion เป็นศัตรูหรือไม่
                    if (targetMinion.getOwner()!= currentMinion.getOwner()) {
                        int damage = Math.max(1, (int) expenditure - targetMinion.getDefense());
                        targetMinion.setHp(Math.max(0, targetMinion.getHp() - damage));
                        if (targetMinion.getHp() <= 0) {
                            targetHex.setMinion(null);
                        }
                    } else {
                        System.out.println("Cannot shoot an ally!");
                    }
                }
            }
        }
    }

    private long getEncodedHexLocation(Hex hex1, Hex hex2) {
        // Implement hex location encoding here.  Example:
        return ((long) hex2.getRow() << 32) | (hex2.getCol() & 0xFFFFFFFFL); // รวม row และ column
    }

    private boolean isAdjacent(Hex hex1, Hex hex2) {
        List<Hex> adjacentHexes = hexGrid.getAdjacentHexes(hex1.getRow(), hex1.getCol());
        return adjacentHexes.contains(hex2);
    }

    public int getHexDistance(Hex hex1, Hex hex2) {
        // Implement hex distance calculation logic here.  Example (Manhattan distance adapted for hexes):
        int dx = Math.abs(hex1.getCol() - hex2.getCol());
        int dy = Math.abs(hex1.getRow() - hex2.getRow());
        int dz = Math.max(0, (dx - dy) / 2); // ปรับสำหรับ hex grid
        return dx + dy + dz;
    }

    public long getAlly() {
        Minion currentMinion = getCurrentMinion();
        if (currentMinion!= null) {
            Hex currentHex = currentMinion.getHex();
            int minDistance = Integer.MAX_VALUE;
            Hex closestAllyHex = null;

            // ตรวจสอบ Hex ทั้งหมดใน Grid
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Hex hex = hexGrid.getHex(row, col);
                    if (hex.getMinion()!= null && hex.getMinion().getOwner() == currentMinion.getOwner() && hex!= currentHex) {
                        int distance = getHexDistance(currentHex, hex);
                        if (distance < minDistance) {
                            minDistance = distance;
                            closestAllyHex = hex;
                        }
                    }
                }
            }

            if (closestAllyHex!= null) {
                return getEncodedHexLocation(currentHex, closestAllyHex);
            }
        }
        return 0;
    }

    public long getNearby(String direction) {
        Minion currentMinion = getCurrentMinion();
        if (currentMinion!= null) {
            Hex currentHex = currentMinion.getHex();
            Hex hexToCheck = getTargetHex(currentHex, direction);
            int distance = 1;

            while (hexToCheck!= null) {
                Minion minion = hexToCheck.getMinion();
                if (minion!= null) {
                    int x = String.valueOf(minion.getHp()).length();
                    int y = String.valueOf(minion.getDefense()).length();
                    long value = 100L * x + 10L * y + distance;
                    return minion.getOwner() == currentMinion.getOwner()? -value: value;
                }
                hexToCheck = getTargetHex(hexToCheck, direction);
                distance++;
            }
        }
        return 0;
    }

    public void executeStrategy(Minion minion) throws IOException {
        String strategy = new String(Files.readAllBytes(Paths.get(minion.getStrategy())));
        List<MinionStrategyToken> tokens = new MinionStrategyLexer(strategy).lex();
        MinionStrategyAST.Statement ast = new MinionStrategyParser(tokens).parse();
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(this);
        evaluator.evaluate(ast);
    }

    // ใน class GameState
    public boolean moveMinion(Minion minion, String direction, Player player) {
        if (minion.getOwner() == player) {
            // หา Hex เป้าหมาย
            Hex targetHex = getTargetHex(minion.getHex(), direction);
            if (targetHex!= null && targetHex.getMinion() == null && player.getBudget() >= 1) {
                // ย้าย Minion
                minion.getHex().setMinion(null);
                targetHex.setMinion(minion);
                minion.setHex(targetHex);
                player.setBudget(player.getBudget() - 1);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // หา Hex เป้าหมายตามทิศทาง
    private Hex getTargetHex(Hex currentHex, String direction) {
        int row = currentHex.getRow();
        int col = currentHex.getCol();
        switch (direction) {
            case "up":
                return hexGrid.getHex(row - 1, col);
            case "down":
                return hexGrid.getHex(row + 1, col);
            //... (เพิ่มเติม case สำหรับทิศทางอื่นๆ)
            default:
                return null;
        }
    }

    // ใน class GameState
    public boolean attackMinion(Minion attacker, Minion target, Player player, int expenditure) {
        if (attacker.getOwner() == player && isAdjacent(attacker.getHex(), target.getHex())) {
            int attackCost = getAttackCost(attacker.getType(), expenditure);
            if (player.getBudget() >= attackCost) {
                // คำนวณ damage
                int damage = Math.max(1, expenditure - target.getDefense());
                target.setHp(Math.max(0, target.getHp() - damage));
                player.setBudget(player.getBudget() - attackCost);

                // ตรวจสอบว่า Minion ตายหรือไม่
                if (target.getHp() <= 0) {
                    target.getHex().setMinion(null);
                }
                return true;
            } else {
                return false; // ไม่สามารถโจมตีได้เนื่องจากมีงบประมาณไม่เพียงพอ
            }
        } else {
            return false; // ไม่สามารถโจมตีได้เนื่องจากไม่ใช่เจ้าของ หรือ Hex ไม่อยู่ติดกัน
        }
    }

    // คำนวณค่าใช้จ่ายในการโจมตีตาม strategy
    private int getAttackCost(Minion.MinionType type, int expenditure) {
        int baseCost = switch (type) {
            case TYPE1 -> 1000;
            case TYPE2 -> 1200;
            case TYPE3 -> 900;
        };
        return baseCost + expenditure + 1; // บวกค่าใช้จ่ายคงที่ 1 หน่วย
    }

    private void endGame() {
        // ประกาศผู้ชนะ หรือ เสมอ
        if (currentTurn >= maxTurns) {
            System.out.println("Game Over! Reached maximum turns.");
            // ตรวจสอบจำนวน minion ของแต่ละฝ่ายเพื่อตัดสินผู้ชนะ
        } else {
            Player winner = player1HasMinions ? player1 : player2;
            System.out.println("Game Over! " + winner.getName() + " wins!");
        }
    }
    // ใน class GameState
    public void startNewTurn() {
        currentTurn++;
        player1.setBudget(player1.getBudget() + Config.TURN_BUDGET);
        player2.setBudget(player2.getBudget() + Config.TURN_BUDGET);
        if (isGameOver()) {
            endGame();
            return;
        }
    }

    public long getOpponent() {
        Minion currentMinion = getCurrentMinion();
        if (currentMinion!= null) {
            Hex currentHex = currentMinion.getHex();
            int minDistance = Integer.MAX_VALUE;
            Hex closestOpponentHex = null;

            // ตรวจสอบ Hex ทั้งหมดใน Grid
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Hex hex = hexGrid.getHex(row, col);
                    if (hex.getMinion()!= null && hex.getMinion().getOwner()!= currentMinion.getOwner()) {
                        int distance = getHexDistance(currentHex, hex);
                        if (distance < minDistance) {
                            minDistance = distance;
                            closestOpponentHex = hex;
                        }
                    }
                }
            }

            if (closestOpponentHex!= null) {
                return getEncodedHexLocation(currentHex, closestOpponentHex);
            }
        }
        return 0;
    }

    private void promptPlayerForPurchase() {
        // แสดงผล budget ปัจจุบัน
        System.out.println("Player " + currentPlayer.getName() + " - Budget: " + currentPlayer.getBudget());

        // รับ input จากผู้เล่น
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nWhat do you want to buy?");
            System.out.println("1. Minion");
            System.out.println("2. Hex");
            System.out.println("3. Nothing");
            System.out.print("Enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                System.out.print("Enter your choice: ");
                scanner.next(); // Clear invalid input
            }

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    buyMinionFromInput(scanner);
                    break;
                case 2:
                    buyHexFromInput(scanner);
                    break;
                case 3:
                    System.out.println("Skipping purchase phase.");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        } while (choice!= 3); // Continue prompting until player chooses "Nothing"
    }

    private void buyMinionFromInput(Scanner scanner) {
        // รับ input ประเภท minion, row, col
        System.out.print("Enter minion type (TYPE1, TYPE2, TYPE3): ");
        String minionType = scanner.nextLine().toUpperCase();
        System.out.print("Enter row: ");
        int row = scanner.nextInt();
        System.out.print("Enter col: ");
        int col = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // ตรวจสอบความถูกต้องของ input
        try {
            Minion.MinionType type = Minion.MinionType.valueOf(minionType);
            if (row < 0 || row >= 8 || col < 0 || col >= 8) {
                System.out.println("Invalid row or column. Please enter values between 0 and 7.");
                return;
            }

            // ดำเนินการซื้อ minion
            if (buyMinion(type, row, col, currentPlayer)) {
                System.out.println("Minion purchased successfully.");
            } else {
                System.out.println("Failed to purchase minion.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid minion type. Please enter TYPE1, TYPE2, or TYPE3.");
        }
        int currentMinionCount = hexGrid.getTotalMinionCount(); // สมมติว่ามี method นี้ใน HexGrid
        if (currentMinionCount >= Config.MAX_SPAWNS) {
            System.out.println("Cannot buy more minions. Maximum number of minions reached.");
            return;
        }
    }

    private void buyHexFromInput(Scanner scanner) {
        // รับ input row, col
        System.out.print("Enter row: ");
        int row = scanner.nextInt();
        System.out.print("Enter col: ");
        int col = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // ตรวจสอบความถูกต้องของ input
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            System.out.println("Invalid row or column. Please enter values between 0 and 7.");
            return;
        }

        // ดำเนินการซื้อ hex
        if (buyHex(row, col, currentPlayer)) {
            System.out.println("Hex purchased successfully.");
        } else {
            System.out.println("Failed to purchase hex.");
        }
    }

    public boolean isGameOver() {
        // 1. ตรวจสอบว่าครบ 100 turns หรือไม่
        if (currentTurn >= maxTurns) {
            return true;
        }

        // 2. ตรวจสอบว่าฝ่ายใดฝ่ายหนึ่งไม่มี minion เหลือหรือไม่
        boolean player1HasMinions = false;
        boolean player2HasMinions = false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Hex hex = hexGrid.getHex(row, col);
                Minion minion = hex.getMinion();
                if (minion!= null) {
                    if (minion.getOwner() == player1) {
                        player1HasMinions = true;
                    } else {
                        player2HasMinions = true;
                    }
                }
            }
        }

        return!player1HasMinions ||!player2HasMinions;
    }


    private void executeMinionStrategies() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Hex hex = hexGrid.getHex(row, col);
                Minion minion = hex.getMinion();
                if (minion!= null) {
                    try {
                        executeStrategy(minion); // เรียกใช้ method ที่มีอยู่แล้ว
                    } catch (IOException e) {
                        System.err.println("Error executing strategy for minion at (" + row + ", " + col + "): " + e.getMessage());
                    }
                }
            }
        }
    }


    public void startGame() {
        // 1. เลือกโหมดเกม
        int mode = getModeFromInput();

        // 2. กำหนดจำนวน minion type
        int numMinionTypes = getNumMinionTypesFromInput();

        // 3. กำหนดชื่อ minion และ defense
        for (int i = 0; i < numMinionTypes; i++) {
            String name = getMinionNameFromInput(i);
            int defense = getMinionDefenseFromInput(i);
            //... (สร้าง minion type)...
        }

        // 4. เขียน strategy
        for (int i = 0; i < numMinionTypes; i++) {
            String strategy = getStrategyFromInput(i);
            //... (บันทึก strategy)...
        }

        // 5. เลือก minion เริ่มต้นและวางบน HexGrid
        for (Player player: Arrays.asList(player1, player2)) {
            Minion.MinionType minionType = getInitialMinionPlacementFromInput(player, minionTypes); // ส่ง minionTypes เป็น argument

            int col = getHexCoordinatesFromInput("col");
            //... (วาง minion บน HexGrid)...
        }

        // 6. เริ่มเกม
        while (!isGameOver()) {
            startNewTurn();
            if (mode == 2 && currentPlayer == player2 || mode == 3) {
                executeBotTurn();
            }
        }

        // 7. จบเกม
        endGame();
    }

    private int getNumMinionTypesFromInput() {
        Scanner scanner = new Scanner(System.in);
        int numMinionTypes;
        do {
            System.out.print("Enter number of minion types (1-3): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                System.out.print("Enter number of minion types (1-3): ");
                scanner.next();
            }
            numMinionTypes = scanner.nextInt();
        } while (numMinionTypes < 1 || numMinionTypes > 3);
        return numMinionTypes;
    }

    private String getMinionNameFromInput(int i) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name for minion type " + (i + 1) + ": ");
        return scanner.nextLine();
    }

    private int getMinionDefenseFromInput(int i) {
        Scanner scanner = new Scanner(System.in);
        int defense;
        do {
            System.out.print("Enter defense for minion type " + (i + 1) + " (200-400): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 200 and 400.");
                System.out.print("Enter defense for minion type " + (i + 1) + " (200-400): ");
                scanner.next();
            }
            defense = scanner.nextInt();
        } while (defense < 200 || defense > 400);
        return defense;
    }

    private String getStrategyFromInput(int i) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter strategy for minion type " + (i + 1) + ": ");
        return scanner.nextLine();
    }

    private Minion.MinionType getInitialMinionPlacementFromInput(Player player, Minion.MinionType[] minionTypes) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println(player.getName() + ", select your initial minion type:");
            for (int i = 0; i < minionTypes.length; i++) {
                System.out.println((i + 1) + ". " + minionTypes[i]);
            }
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                System.out.print("Enter your choice: ");
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
            System.out.print("Enter " + coordinate + " coordinate (0-7): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 0 and 7.");
                System.out.print("Enter " + coordinate + " coordinate (0-7): ");
                scanner.next();
            }
            value = scanner.nextInt();
        } while (value < 0 || value > 7);
        return value;
    }

    private void executeBotTurn() {
        System.out.println("Bot is thinking...");
    }

    private int getModeFromInput() {
            Scanner scanner = new Scanner(System.in);
            int mode;
            do {
                System.out.println("Select game mode:");
                System.out.println("1. Player vs Player");
                System.out.println("2. Player vs Bot");
                System.out.println("3. Bot vs Bot");
                System.out.print("Enter your choice: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number.");
                    System.out.print("Enter your choice: ");
                    scanner.next();
                }
                mode = scanner.nextInt();
            } while (mode < 1 || mode > 3);
            return mode;
        }

    }


