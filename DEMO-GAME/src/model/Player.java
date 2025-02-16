package main.model;

import java.util.ArrayList;
import java.util.List;
import main.minion.MinionStrategyAST; // Import MinionStrategyAST
import main.minion.MinionStrategyParser;  // Import the parser
import main.minion.MinionStrategyLexer;   // Import the lexer
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import main.config.Config; // Import Config



public class Player {
    private String name; // ชื่อของผู้เล่น
    private int budget; // งบประมาณของผู้เล่น
    private List<Minion> minions; // ลิสต์ของ Minion ที่ผู้เล่นเป็นเจ้าของ
    private int inputDefense; // ค่า inputDefense ที่ผู้เล่นกำหนด  (ใช้ตอนสร้าง Minion)
    private Map<String, Long> variables; // ตัวแปร (ใช้สำหรับ Minion strategies)


    public Player(String name, int initialBudget) {
        this.name = name;
        this.budget = initialBudget; // ใช้ initialBudget ที่รับมา
        this.minions = new ArrayList<>();
        this.inputDefense = 250; // ค่า defense เริ่มต้น (default value)
        this.variables = new HashMap<>(); // เริ่มต้นตัวแปร
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }
    public void addBudget(int budget){ // เมธอดสำหรับเพิ่ม budget
        this.budget += budget;
    }

    public List<Minion> getMinions() {
        return minions;
    }

    public void setMinions(List<Minion> minions) {
        this.minions = minions;
    }
    public void addMinion(Minion minion){
        this.minions.add(minion); // เพิ่ม Minion เข้าไปใน list
    }

    public int getInputDefense() {
        return inputDefense;
    }

    public void setInputDefense(int inputDefense) {
        this.inputDefense = inputDefense;
    }
    public Map<String, Long> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Long> variables) {
        this.variables = variables;
    }


    // ซื้อ Minion
    public boolean buyMinion(Minion.MinionType minionType, int row, int col, HexGrid hexGrid) {
        if (budget >= minionType.getCost() && minions.size() < Config.MAX_SPAWNS) { //ใช้ Config.MAX_SPAWNS) { //ใช้ Config.MAX_SPAWNS
            Hex hex = hexGrid.getHex(row, col);

            // ตรวจสอบว่า hex เป็นของผู้เล่นและว่าง
            if (hex != null && hex.getOwner() == this && hex.getMinion() == null) {
                MinionStrategyAST.Statement strategy = loadStrategy(minionType.getStrategyPath()); // โหลด strategy
                if(strategy == null) return false; // ถ้าโหลด strategy ไม่สำเร็จ
                Minion newMinion = new Minion(minionType.getHp(), this.inputDefense, strategy, this); // สร้าง Minion ใหม่, ใช้ minionType.getHp() และ this.inputDefense
                newMinion.setHex(hex); // วาง Minion ลงใน hex
                hex.setMinion(newMinion);  // อัพเดท hex
                minions.add(newMinion); // เพิ่ม Minion เข้า list
                budget -= minionType.getCost(); // ลด budget

                return true; // ซื้อ Minion สำเร็จ
            }
        }
        return false; // ซื้อ Minion ไม่สำเร็จ
    }

    // ซื้อ Hex
    public boolean buyHex(int row, int col, HexGrid hexGrid) {
        Hex hex = hexGrid.getHex(row, col);
        if (budget >= Config.HEX_PURCHASE_COST && hex != null && hex.getOwner() == null) {
            //  ตรวจสอบว่ามี Hex ข้างเคียงที่เป็นของผู้เล่นอยู่ หรือ เป็นการซื้อ hex ครั้งแรก
            List<Hex> adjacentHexes = hexGrid.getAdjacentHexesOwnedByPlayer(row, col, this);
            if (!adjacentHexes.isEmpty() || this.minions.isEmpty()) {
                budget -= Config.HEX_PURCHASE_COST; // ลด budget
                hex.setOwner(this); // กำหนดให้ผู้เล่นเป็นเจ้าของ Hex
                return true; // ซื้อ Hex สำเร็จ
            }

        }
        return false; // ซื้อ Hex ไม่สำเร็จ
    }

    private MinionStrategyAST.Statement loadStrategy(String strategyPath) {
        try {
            // อ่านไฟล์ strategy
            String strategyCode = new String(Files.readAllBytes(Paths.get("src/main/minion/" + strategyPath.replace(".", "/") + ".txt")));
            MinionStrategyLexer lexer = new MinionStrategyLexer(strategyCode);
            List<main.minion.MinionStrategyToken> tokens = lexer.lex();
            MinionStrategyParser parser = new MinionStrategyParser(tokens);
            return parser.parse(); // คืนค่า AST
        } catch (IOException e) {
            System.err.println("Error loading strategy file: " + e.getMessage());
            return null; // เกิดข้อผิดพลาดในการโหลด strategy
        }
    }

    public void executeMinionStrategies(HexGrid hexGrid, GameState gameState) {
        for (Minion minion : this.getMinions()) {
            if (minion.getHex() != null) { // ตรวจสอบว่า Minion อยู่บนกระดานหรือไม่
                main.minion.MinionStrategyEvaluator evaluator = new main.minion.MinionStrategyEvaluator(gameState, minion); // Pass Minion object and GameState
                // ส่งผ่าน variables จาก GameState ไปยัง MinionStrategyEvaluator ไม่ต้องทำเเล้วเพราะใช้ shared map
                // evaluator.setVariables(gameState.getVariables());

                evaluator.evaluate(minion.getStrategy());
            }
        }
    }



    public void promptPlayerTurn(HexGrid hexGrid, GameState gameState) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(this.getName() + ", it's your turn. Budget: " + this.getBudget());

        // แสดงตัวเลือก
        System.out.println("Choose an action:");
        System.out.println("1. Move Minion");
        System.out.println("2. Attack with Minion");
        System.out.println("3. Pass Turn");

        int actionChoice;
        do {
            System.out.print("Enter your choice (1-3): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                scanner.next(); // Consume invalid input
            }
            actionChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline
        } while (actionChoice < 1 || actionChoice > 3);


        switch (actionChoice) {
            case 1: // Move Minion
                Minion minionToMove = selectMinion(scanner); // เลือก Minion ที่จะสั่ง
                if (minionToMove != null) {
                    String direction = selectDirection(scanner); // เลือกทิศทาง
                    gameState.move(direction);  // ใช้เมธอด move ของ GameState
                }
                break;

            case 2: // Attack
                Minion minionToAttackWith = selectMinion(scanner);  //เลือก Minion ที่จะสั่ง
                if (minionToAttackWith != null) {
                    String direction = selectDirection(scanner); // เลือกทิศทาง
                    long expenditure = selectExpenditure(scanner); //  เลือกจำนวนเงิน

                    gameState.shoot(direction, expenditure); // ใช้เมธอด shoot ของ GameState

                }
                break;
            case 3: // Pass Turn
                System.out.println("Passing turn.");
                break;
        }

    }
    // Helper methods for promptPlayerTurn

    private Minion selectMinion(Scanner scanner) {
        System.out.println("Select a minion to command:");
        for (int i = 0; i < minions.size(); i++) {
            Minion minion = minions.get(i);
            if (minion.getHex() != null) { // แสดงเฉพาะ Minion ที่อยู่บนกระดาน
                System.out.println((i + 1) + ". " + minion.getMinionType().getName() + " at (" + minion.getHex().getRow() + ", " + minion.getHex().getCol() + ")");
            }
        }
        int minionChoice;
        do {
            System.out.print("Enter your choice (1-" + minions.size() + "): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume invalid input
            }
            minionChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } while (minionChoice < 1 || minionChoice > minions.size() || minions.get(minionChoice -1).getHex() == null);

        return minions.get(minionChoice - 1); // คืนค่า Minion ที่ถูกเลือก
    }

    private String selectDirection(Scanner scanner) {
        System.out.println("Select a direction:");
        System.out.println("1. up");
        System.out.println("2. down");
        System.out.println("3. upleft");
        System.out.println("4. upright");
        System.out.println("5. downleft");
        System.out.println("6. downright");

        int directionChoice;
        do {
            System.out.print("Enter your choice (1-6): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 6.");
                scanner.next(); // Consume invalid input
            }
            directionChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
        } while (directionChoice < 1 || directionChoice > 6);

        String direction = "";
        switch (directionChoice) {
            case 1: direction = "up"; break;
            case 2: direction = "down"; break;
            case 3: direction = "upleft"; break;
            case 4: direction = "upright"; break;
            case 5: direction = "downleft"; break;
            case 6: direction = "downright"; break;
        }
        return direction; // คืนค่าทิศทางที่เลือก
    }

    private long selectExpenditure(Scanner scanner){
        System.out.print("Enter expenditure for attack: ");
        while(!scanner.hasNextLong()){
            System.out.print("Invalid input. Enter expenditure (long): ");
            scanner.next(); // Consume invalid input
        }
        long expenditure = scanner.nextLong();
        scanner.nextLine(); // Consume newline
        return expenditure; // คืนค่าจำนวนเงินที่เลือก

    }
}