package project.kombat1.model;

import project.kombat1.config.Config;
import project.kombat1.minion.MinionStrategyAST;
import project.kombat1.minion.MinionStrategyEvaluator;
import project.kombat1.minion.MinionStrategyLexer;
import project.kombat1.minion.MinionStrategyParser;
import project.kombat1.minion.MinionStrategyToken;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {

    private String name;
    private int budget;
    private List<Minion> minions;
    private Minion currentMinion;


    public Player(String name, int budget) {
        this.name = name;
        this.budget = budget;
        this.minions = new ArrayList<>();
    }

    // Getters and Setters
    public String getName() { return name; }
    public int getBudget() { return budget; }
    public void setBudget(int budget) { this.budget = budget; }
    public List<Minion> getMinions() { return minions; }
    public Minion getCurrentMinion() {
        for(Minion minion : minions){
            if(minion.isCurrent()){
                return minion;
            }
        }
        return null;
    }

    public void setCurrentMinion(Minion minion){
        if(currentMinion!= null){
            currentMinion.setCurrent(false);
        }
        currentMinion = minion;
        if(currentMinion!= null){
            currentMinion.setCurrent(true);
        }
    }



    // ซื้อ Minion
    public boolean buyMinion(Minion.MinionType type, int row, int col, HexGrid hexGrid) {
        int minionCost = type.getCost();
        if (this.budget >= minionCost && hexGrid.isHexEmpty(row, col) && this.minions.size() < Config.MAX_SPAWNS) {
            Minion minion = new Minion(type.getName(), 0, type.getStrategyPath(), hexGrid.getHex(row, col), this, type); // สร้าง Minion
            hexGrid.getHex(row, col).setMinion(minion); // วาง Minion บน Hex
            this.budget -= minionCost; // หักเงิน
            this.minions.add(minion);   // เพิ่ม Minion ใน list
            return true;
        } else {
            // แจ้ง error (optional)
            if (this.minions.size() >= Config.MAX_SPAWNS) {
                System.out.println("ไม่สามารถซื้อ Minion เพิ่มได้: จำนวน Minion สูงสุด");
            } else if (this.budget < minionCost) {
                System.out.println("ไม่สามารถซื้อ Minion: งบไม่พอ");
            } else {
                System.out.println("ไม่สามารถซื้อ Minion: Hex ไม่ว่าง");
            }
            return false;
        }
    }

    // เคลื่อนที่ Minion
    public void move(String direction, HexGrid hexGrid, GameState gameState) {
        Minion currentMinion = getCurrentMinion(); // Minion ที่ถูกเลือก
        if (currentMinion != null) {
            Hex targetHex = gameState.getTargetHex(currentMinion.getHex(), direction);  // Hex เป้าหมาย
            if (targetHex != null && targetHex.getMinion() == null && budget >= 1) { // ตรวจสอบ Hex และ budget

                // ตรวจสอบว่า Hex เป็นของ Player คนนี้หรือไม่
                if (targetHex.getOwner() != this) {
                    System.out.println("คุณไม่ได้เป็นเจ้าของ hex นั้น");
                    return;
                }

                currentMinion.getHex().setMinion(null); // เอา Minion ออกจาก Hex เดิม
                targetHex.setMinion(currentMinion);     // ย้าย Minion ไป Hex ใหม่
                currentMinion.setHex(targetHex);         // อัปเดต Hex ของ Minion
                budget--;                                // หัก budget
            } else {
                System.out.println("การเคลื่อนที่ไม่ถูกต้อง"); // แจ้ง error
            }
        }
        else {
            System.out.println("ไม่มี minion ที่เลือก.");
        }
    }

    // ยิง
    public void shoot(String direction, long expenditure, HexGrid hexGrid, GameState gameState) {
        Minion currentMinion = getCurrentMinion();
        if (currentMinion!= null) {
            Hex targetHex = gameState.getTargetHex(currentMinion.getHex(), direction);
            if (targetHex!= null && budget >= expenditure + 1) {
                budget -= expenditure + 1;
                Minion targetMinion = targetHex.getMinion();
                if (targetMinion!= null) {
                    if (targetMinion.getOwner()!= this) {
                        int damage = Math.max(1, (int) expenditure - targetMinion.getDefense());
                        targetMinion.setHp(Math.max(0, targetMinion.getHp() - damage));
                        if (targetMinion.getHp() <= 0) {
                            targetHex.setMinion(null);
                            targetMinion.getOwner().getMinions().remove(targetMinion);
                        }
                    } else {
                        System.out.println("ไม่สามารถยิง Minion ฝ่ายเดียวกันได้!");
                    }
                }else {
                    System.out.println("ไม่มี minion ที่ hex เป้าหมาย");
                }
            }else {
                System.out.println("การกระทำ shoot ไม่ถูกต้อง หรือ งบไม่พอ");
            }
        } else {
            System.out.println("ไม่มี minion ที่เลือก.");
        }
    }

    // หา Minion ฝ่ายเดียวกันที่ใกล้ที่สุด
    public long getAlly(HexGrid hexGrid, GameState gameState) {
        Minion currentMinion = getCurrentMinion();
        if(currentMinion != null){
            Hex currentHex = currentMinion.getHex();
            int minDistance = Integer.MAX_VALUE;
            Hex closestAlly = null;

            for(Minion ally : this.minions){ // วนลูป Minion ทั้งหมดของ Player นี้
                if(ally != currentMinion){  // ไม่นับตัวเอง
                    Hex hex = ally.getHex();
                    int distance = gameState.getHexDistance(currentHex, hex);
                    if(distance < minDistance){
                        minDistance = distance;
                        closestAlly = hex;
                    }
                }
            }
            if(closestAlly != null){
                return gameState.getEncodedHexLocation(currentHex, closestAlly); // เข้ารหัสตำแหน่ง
            }
        }
        return 0; // ไม่เจอ Minion ฝ่ายเดียวกัน
    }



    // หา Minion ในทิศทางที่กำหนด
    public long getNearby(String direction, HexGrid hexGrid, GameState gameState) {
        Minion currentMinion = getCurrentMinion();
        if (currentMinion!= null) {
            Hex currentHex = currentMinion.getHex();
            Hex hexToCheck = gameState.getTargetHex(currentHex, direction); // hex ถัดไป
            int distance = 1;

            while (hexToCheck!= null) {
                Minion minion = hexToCheck.getMinion();
                if (minion!= null) {
                    int x = String.valueOf(minion.getHp()).length(); // จำนวนหลักของ hp
                    int y = String.valueOf(minion.getDefense()).length(); // จำนวนหลักของ defense

                    long value = 100L * x + 10L*y + distance; // เข้ารหัสข้อมูล
                    return minion.getOwner() == this ? -value : value; // คืนค่า + หรือ -
                }
                hexToCheck = gameState.getTargetHex(hexToCheck, direction); // hex ถัดไป
                distance++;
            }
        }
        return 0; // ไม่เจอ
    }


    // เรียกใช้ Strategy ของ Minion (เรียกจาก GameState)
    public void executeStrategy(Minion minion, HexGrid hexGrid, GameState gameState) throws IOException {
        minion.executeStrategy(hexGrid, gameState);
    }

    // เรียกใช้ Strategy ของ Minions ทั้งหมด
    public void executeMinionStrategies(HexGrid hexGrid, GameState gameState) {
        for (Minion minion : this.getMinions()) {
            try {
                executeStrategy(minion, hexGrid, gameState); // เรียก executeStrategy ของ Minion แต่ละตัว
            } catch (IOException e) {
                System.err.println("เกิดข้อผิดพลาดในการรัน Strategy ของ Minion: " + e.getMessage());
            }
        }
    }

    // หา Minion ศัตรูที่ใกล้ที่สุด
    public long getOpponent(HexGrid hexGrid, GameState gameState) {
        Minion currentMinion = getCurrentMinion();
        if(currentMinion != null){
            Hex currentHex = currentMinion.getHex();
            int minDistance = Integer.MAX_VALUE;
            Hex closestOpponent = null;
            Player opponent = (this == gameState.getPlayer1()) ? gameState.getPlayer2() : gameState.getPlayer1();
            for(Minion opponentMinion : opponent.getMinions()){ // วนลูป Minion ของฝ่ายตรงข้าม
                Hex hex = opponentMinion.getHex();
                int distance = gameState.getHexDistance(currentHex,hex);
                if(distance < minDistance){
                    minDistance = distance;
                    closestOpponent = hex;
                }
            }

            if(closestOpponent != null){
                return gameState.getEncodedHexLocation(currentHex, closestOpponent); // เข้ารหัสตำแหน่ง
            }
        }
        return 0; // ไม่เจอ Minion ฝ่ายตรงข้าม
    }

    // ควบคุม Turn ของ Player
    public void promptPlayerTurn(HexGrid hexGrid, GameState gameState) {
        Scanner scanner = new Scanner(System.in);
        if(this.minions.isEmpty()){
            System.out.println(this.name + ", คุณไม่มี Minion เหลืออยู่");
            return;
        }

        // เลือก Minion
        Minion selectedMinion = null;
        do {
            System.out.println(this.getName() + ", เลือก Minion ที่จะสั่งการ (ใส่แถวและคอลัมน์):");
            for (int i = 0; i < this.minions.size(); i++) {
                Minion m = this.minions.get(i);
                System.out.println((i + 1) + ". Minion ที่ (" + m.getHex().getRow() + ", " + m.getHex().getCol() + ")");
            }
            System.out.print("ใส่หมายเลข Minion: ");
            int minionChoice = scanner.nextInt();
            scanner.nextLine();
            if(minionChoice > 0 && minionChoice <= this.minions.size()){
                selectedMinion = this.minions.get(minionChoice -1 );
                this.setCurrentMinion(selectedMinion);
            } else {
                System.out.println("การเลือก Minion ไม่ถูกต้อง");
            }


        } while(selectedMinion == null);

        // เลือก Action
        int actionChoice;

        do {
            System.out.println(this.getName() + ", เลือกการกระทำสำหรับ Minion ของคุณ:");
            System.out.println("1. เคลื่อนที่");
            System.out.println("2. ยิง");
            System.out.println("3. ข้าม");
            System.out.print("เลือก: ");

            while (!scanner.hasNextInt()) {
                System.out.println("กรุณาใส่ตัวเลข");
                System.out.print("เลือก: ");
                scanner.next();
            }

            actionChoice = scanner.nextInt();
            scanner.nextLine();

            switch (actionChoice) {
                case 1:
                    System.out.print("ใส่ทิศทางที่จะเคลื่อนที่ (up, down, up_left, up_right, down_left, down_right): ");
                    String moveDirection = scanner.nextLine();
                    this.move(moveDirection, hexGrid, gameState); // เรียก move
                    break;
                case 2:
                    System.out.print("ใส่ทิศทางที่จะยิง (up, down, up_left, up_right, down_left, down_right): ");
                    String shootDirection = scanner.nextLine();
                    System.out.print("ใส่จำนวนงบประมาณที่จะใช้ยิง: ");
                    long expenditure = scanner.nextLong();
                    scanner.nextLine();
                    this.shoot(shootDirection, expenditure, hexGrid, gameState); // เรียก shoot
                    break;
                case 3:
                    System.out.println("ข้ามเทิร์น");
                    break;
                default:
                    System.out.println("การเลือก Action ไม่ถูกต้อง");
            }
        } while (actionChoice != 1 && actionChoice != 2 && actionChoice != 3); // ทำซ้ำถ้าเลือกไม่ถูกต้อง
    }
}