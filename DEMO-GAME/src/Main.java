package main;

import main.model.*; // Import all classes from the model package
import main.minion.*; // Import all classes from the minion package
import main.config.Config; // Import Config

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // รับชื่อผู้เล่น
        System.out.print("ชื่อผู้เล่น 1: ");
        String player1Name = scanner.nextLine();
        System.out.print("ชื่อผู้เล่น 2: ");
        String player2Name = scanner.nextLine();

        // สร้างผู้เล่น  *แก้ไข: เรียกใช้ constructor ที่ถูกต้อง*
        Player player1 = new Player(player1Name, Config.INIT_BUDGET);
        Player player2 = new Player(player2Name, Config.INIT_BUDGET);

        // Set initial inputDefense for players.  This assumes you've added the
        // setInputDefense method to your Player class.
        System.out.print("Enter inputDefense for " + player1.getName() + ": ");
        player1.setInputDefense(scanner.nextInt());
        scanner.nextLine(); // Consume the newline

        System.out.print("Enter inputDefense for " + player2.getName() + ": ");
        player2.setInputDefense(scanner.nextInt());
        scanner.nextLine(); // Consume the newline

        // สร้าง GameState
        GameState gameState = new GameState(player1, player2);

        // Initial Minion placement and Hex ownership (simplified for demonstration)
        // เริ่มเกมด้วย Minion ฟรี และ Hex เริ่มต้น
        setupInitialGameState(player1, player2, gameState.getHexGrid());


        // วนลูปเกมจนกว่าจะจบ
        while (!gameState.isGameOver()) {
            // อัพเดทสถานะเกม
            gameState.update();

            // แสดงผลสถานะเกมในคอนโซล
            displayGameState(gameState);

            // รับ input จากผู้เล่นในแต่ละเทิร์น
            // กำหนดผู้เล่นปัจจุบัน
            Player currentPlayer = (gameState.getCurrentTurn() % 2 == 1) ? player1 : player2;  // แก้ไขการสลับเทิร์นให้ถูกต้อง
            System.out.println("\nTurn of " + currentPlayer.getName());
            processPlayerInput(scanner, currentPlayer, gameState); // ดำเนินการตาม input

            // ตรวจสอบเงื่อนไขจบเกมหลังจากการกระทำของผู้เล่นแต่ละคน
            if(gameState.isGameOver()){
                break;
            }

        }

        // แสดงผลผู้ชนะ
        Player winner = gameState.getWinner();
        if (winner != null) {
            System.out.println("ผู้ชนะคือ " + winner.getName() + "!");
        } else {
            System.out.println("เสมอกัน!");
        }

        scanner.close();
    }


    private static void displayGameState(GameState gameState) {
        HexGrid hexGrid = gameState.getHexGrid();
        Player player1 = gameState.getPlayer1();
        Player player2 = gameState.getPlayer2();

        System.out.println("\n===== สถานะเกม =====");
        System.out.println("เทิร์น: " + gameState.getCurrentTurn());

        // แสดง HexGrid
        System.out.println("\n--- Hex Grid ---");
        displayHexGrid(hexGrid, player1, player2);


        // แสดงข้อมูลผู้เล่น
        System.out.println("\n--- ข้อมูลผู้เล่น ---");
        displayPlayerInfo(player1);
        displayPlayerInfo(player2);


    }
    private static void displayHexGrid(HexGrid hexGrid, Player player1, Player player2) {
        // Hex Grid display logic (as provided before, but adjusted for Hexagonal grid)

        int numRows = hexGrid.getNumRows();
        int numCols = hexGrid.getNumCols();

        // Adjust spacing for hexagonal grid
        System.out.print("   "); // Initial spacing for row numbers
        for (int col = 0; col < numCols; col++) {
            System.out.printf("%-4d", col); // Column numbers, adjusted spacing
        }
        System.out.println();


        for (int row = 0; row < numRows; row++) {
            // Indentation for odd rows
            if (row % 2 != 0) {
                System.out.print("  "); // Two spaces for odd rows
            }
            System.out.printf("%-3d", row); // Row number with right padding


            for (int col = 0; col < numCols; col++) {
                Hex hex = hexGrid.getHex(row, col);
                String cellContent = " . "; // Default empty hex

                if (hex.getMinion() != null) {
                    Minion minion = hex.getMinion();
                    if (minion.getOwner() == player1) {
                        cellContent = " P1"; // Player 1 minion
                    } else if (minion.getOwner() == player2) {
                        cellContent = " P2"; // Player 2 minion
                    }
                } else if (hex.getOwner() != null) {
                    if (hex.getOwner() == player1) {
                        cellContent = " H1";  //Player 1 Hex
                    }else if (hex.getOwner() == player2){
                        cellContent = " H2"; // Player 2 Hex
                    }
                }

                System.out.print(cellContent + " "); // Add extra space for visual separation

            }
            System.out.println(); // Newline after each row
        }
    }
    private static void displayPlayerInfo(Player player) {
        System.out.println(player.getName() + ":");
        System.out.println("  งบประมาณ: " + player.getBudget());
        System.out.println("  Minions:");
        for (int i = 0; i < player.getMinions().size(); i++) {
            Minion minion = player.getMinions().get(i);
            if (minion.getHex() != null) { // Minion on the board
                System.out.printf("    %d. %s (HP: %d, Defense: %d) ที่ (%d, %d)%n",
                        i + 1, minion.getMinionType().getName() , minion.getHp(), minion.getDefense(),
                        minion.getHex().getRow(), minion.getHex().getCol());
            } else{ // Minion not on the board
                System.out.printf("    %d. %s (HP: %d, Defense: %d) [ไม่ได้อยู่บนกระดาน]%n",
                        i + 1, minion.getMinionType().getName() , minion.getHp(), minion.getDefense());
            }

        }
        System.out.println("---------");
    }

    private static void processPlayerInput(Scanner scanner, Player currentPlayer, GameState gameState) {
        while (true) { // วนซ้ำจนกว่าจะมีการกระทำที่ถูกต้อง หรือผู้เล่นเลือกที่จะผ่านเทิร์น
            System.out.print("ป้อนคำสั่ง (buy minion<type>, buy hex <row> <col>, pass): ");
            String command = scanner.nextLine();
            String[] parts = command.split(" ");

            if (parts.length > 0) {
                switch (parts[0]) {
                    case "buy":
                        if (parts.length > 2 && parts[1].equals("minion")) {
                            try {
                                Minion.MinionType type = Minion.MinionType.valueOf("TYPE" + parts[2].toUpperCase());
                                // หา hex ที่ว่างสำหรับผู้เล่น
                                List<Hex> availableHexes = getAvailableHexes(currentPlayer, gameState.getHexGrid());

                                if(availableHexes.isEmpty()){
                                    System.out.println("ไม่มี hex ว่างสำหรับวาง Minion.");
                                    break;
                                }

                                // แสดง hex ที่ว่าง
                                System.out.println("Hex ที่ว่างสำหรับวาง Minion:");
                                for (int i = 0; i < availableHexes.size(); i++) {
                                    Hex hex = availableHexes.get(i);
                                    System.out.println((i + 1) + ": (" + hex.getRow() + ", " + hex.getCol() + ")");
                                }

                                // รับ input ตำแหน่ง hex ที่ต้องการ
                                System.out.print("เลือกหมายเลข hex ที่จะวาง Minion: ");
                                int hexChoice = scanner.nextInt();
                                scanner.nextLine(); // consume newline

                                if (hexChoice > 0 && hexChoice <= availableHexes.size()) {
                                    Hex selectedHex = availableHexes.get(hexChoice - 1);
                                    if (currentPlayer.buyMinion(type, selectedHex.getRow(), selectedHex.getCol(), gameState.getHexGrid())) {
                                        System.out.println("Minion " + type + " ถูกซื้อและวางที่ (" + selectedHex.getRow() + ", " + selectedHex.getCol() + ")");
                                        return; // ออกหลังจากดำเนินการสำเร็จ
                                    } else {
                                        System.out.println("ไม่สามารถซื้อ Minion ได้");
                                    }
                                } else {
                                    System.out.println("เลือก hex ไม่ถูกต้อง");
                                }

                            } catch (IllegalArgumentException e) {
                                System.out.println("ประเภท Minion ไม่ถูกต้อง");
                            }
                            catch (InputMismatchException e){
                                System.out.println("Input ไม่ถูกต้อง");
                                scanner.next(); // clear the invalid input
                            }
                        } else if (parts.length == 4 && parts[1].equals("hex")) {
                            try {
                                int row = Integer.parseInt(parts[2]);
                                int col = Integer.parseInt(parts[3]);
                                if (currentPlayer.buyHex(row, col, gameState.getHexGrid())) {
                                    System.out.println("Hex ที่ (" + row + ", " + col + ") ถูกซื้อ");
                                    return;  // ออกหลังจากดำเนินการสำเร็จ

                                } else {
                                    System.out.println("ไม่สามารถซื้อ Hex ได้");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("หมายเลขแถวหรือคอลัมน์ไม่ถูกต้อง");
                            }
                        } else {
                            System.out.println("รูปแบบคำสั่งซื้อไม่ถูกต้อง");
                        }
                        break;
                    case "pass":
                        System.out.println("ผ่านเทิร์น");
                        return; // ออกหลังจากดำเนินการสำเร็จ
                    default:
                        System.out.println("คำสั่งไม่ถูกต้อง");
                }
            }
        }
    }

    private static List<Hex> getAvailableHexes(Player player, HexGrid hexGrid) {
        List<Hex> availableHexes = new ArrayList<>();

        if (player.getMinions().isEmpty()) {
            // ถ้าผู้เล่นไม่มี Minion ผู้เล่นสามารถวาง Minion ตัวแรกบน Hex ใดก็ได้ที่ว่าง
            for (int row = 0; row < hexGrid.getNumRows(); row++) {
                for (int col = 0; col < hexGrid.getNumCols(); col++) {
                    Hex hex = hexGrid.getHex(row, col);
                    if (hex.getMinion() == null && hex.getOwner() == null) {
                        availableHexes.add(hex);
                    }
                }
            }
        } else {
            // ถ้าผู้เล่นมี Minion อยู่แล้ว ผู้เล่นสามารถวาง Minion ใหม่บน Hex ที่ผู้เล่นเป็นเจ้าของและว่างอยู่เท่านั้น
            for (int row = 0; row < hexGrid.getNumRows(); row++) {
                for (int col = 0; col < hexGrid.getNumCols(); col++) {
                    Hex hex = hexGrid.getHex(row, col);
                    if (hex.getOwner() == player && hex.getMinion() == null) {
                        availableHexes.add(hex);
                    }
                }
            }
        }
        return availableHexes;
    }

    private static void setupInitialGameState(Player player1, Player player2, HexGrid hexGrid) {
        // Player 1 gets the top-left corner hexes
        hexGrid.getHex(0, 0).setOwner(player1);
        hexGrid.getHex(0, 1).setOwner(player1);
        hexGrid.getHex(1, 0).setOwner(player1);
        hexGrid.getHex(1, 1).setOwner(player1);
        hexGrid.getHex(2, 0).setOwner(player1);



        // Player 2 gets the bottom-right corner hexes
        hexGrid.getHex(7, 7).setOwner(player2);
        hexGrid.getHex(7, 6).setOwner(player2);
        hexGrid.getHex(6, 7).setOwner(player2);
        hexGrid.getHex(6, 6).setOwner(player2);
        hexGrid.getHex(5, 7).setOwner(player2);


        // Place initial minions.  In a real game, you'd probably let players choose.
        player1.buyMinion(Minion.MinionType.TYPE1, 0, 0, hexGrid);
        player2.buyMinion(Minion.MinionType.TYPE2, 7, 7, hexGrid);
    }

}