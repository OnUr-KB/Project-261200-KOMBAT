package kombat.project1_1.model;

import java.util.List;
import kombat.project1_1.config.Config;
import kombat.project1_1.player.Cheesebear;
import kombat.project1_1.player.Moonum;
import kombat.project1_1.player.Pandy;
import kombat.project1_1.service.GameService;
import lombok.Getter;
import java.util.ArrayList;
import java.util.Scanner;

public class GameState {
    @Getter
    private int currentTurn;
    private final int maxTurns;
    @Getter
    private final Player player1;
    @Getter
    private final Player player2;
    @Getter
    private final HexGrid hexGrid;
    private GameService gameService;

    public GameState(Player player1, Player player2, HexGrid hexGrid, int maxTurns, Player startingPlayer) {
        this.player1 = player1;
        this.player2 = player2;
        this.hexGrid = hexGrid;
        this.maxTurns = maxTurns > 0? maxTurns: Config.MAX_TURNS;
        this.currentTurn = 1;

        this.gameService = gameService;
        player1.buyMinion(new Pandy(gameService), hexGrid.getHex(0, 0)); // ส่ง gameService
        player2.buyMinion(new Pandy(gameService), hexGrid.getHex(7, 7)); // ส่ง gameService
    }

    public void nextTurn() {
        currentTurn++;

        // เพิ่มเงินและคำนวณดอกเบี้ยให้ผู้เล่น 1
        player1.addMoney(Config.TURN_BUDGET);
        player1.earnInterest();

        // ให้ผู้เล่น 1 เลือกซื้อมินเนี่ยน/ช่อง hex เพิ่ม
        Scanner scanner = new Scanner(System.in);
        System.out.println("Player 1, do you want to buy anything? (y/n)");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("y")) {
            System.out.println("1. Buy Pandy (cost: 10000)");
            System.out.println("2. Buy Cheesebear (cost: 12000)");
            System.out.println("3. Buy Moonum (cost: 15000)");
            System.out.println("4. Buy hex (cost: 5000)");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (choice == 1 && player1.getCurrentMoney() >= 1000) {
                System.out.println("Enter the x and y coordinates of the hex to place Pandy:");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                scanner.nextLine(); // consume newline
                Hex hex = hexGrid.getHex(x, y);
                if (hex!= null && hex.getOwner() == player1 &&!hex.isOccupied()) {
                    player1.buyMinion(new Pandy(gameService), hex);
                    System.out.println("Pandy placed at (" + x + ", " + y + ")");
                } else {
                    System.out.println("Invalid hex!");
                }
            } else if (choice == 2 && player1.getCurrentMoney() >= 1200) {
                System.out.println("Enter the x and y coordinates of the hex to place Cheesebear:");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                scanner.nextLine(); // consume newline
                Hex hex = hexGrid.getHex(x, y);
                if (hex!= null && hex.getOwner() == player1 &&!hex.isOccupied()) {
                    player1.buyMinion(new Cheesebear(gameService), hex);
                    System.out.println("Cheesebear placed at (" + x + ", " + y + ")");
                } else {
                    System.out.println("Invalid hex!");
                }
            } else if (choice == 3 && player1.getCurrentMoney() >= 1500) {
                System.out.println("Enter the x and y coordinates of the hex to place Moonum:");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                scanner.nextLine(); // consume newline
                Hex hex = hexGrid.getHex(x, y);
                if (hex!= null && hex.getOwner() == player1 &&!hex.isOccupied()) {
                    player1.buyMinion(new Moonum(gameService), hex);
                    System.out.println("Moonum placed at (" + x + ", " + y + ")");
                } else {
                    System.out.println("Invalid hex!");
                }
            }else if (choice == 4 && player1.getCurrentMoney() >= 2000) {
                System.out.println("Enter the x and y coordinates of the hex to buy:");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                scanner.nextLine(); // consume newline
                Hex hex = hexGrid.getHex(x, y);
                if (hex != null && hex.getOwner() == null && player1.isAdjacentToOwnedHex(hex)) {
                    player1.buyHex(hex);
                    System.out.println("Hex bought at (" + x + ", " + y + ")");
                } else {
                    System.out.println("Invalid hex!");
                }
            }
        }


        // ผู้เล่น 1 โจมตี
        for (Minion minion: player1.getMinions()) {
            if (minion.isAlive()) {
                minion.executeStrategy(this, player1);
            }
        }

        // เพิ่มเงินและคำนวณดอกเบี้ยให้ผู้เล่น 2
        player2.addMoney(Config.TURN_BUDGET);
        player2.earnInterest();

        // ให้ผู้เล่น 2 เลือกซื้อมินเนี่ยน/ช่อง hex เพิ่ม
        System.out.println("Player 2, do you want to buy anything? (y/n)");
        answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("y")) {
            System.out.println("1. Buy Pandy (cost: 1000)");
            System.out.println("2. Buy Cheesebear (cost: 1200)");
            System.out.println("3. Buy Moonum (cost: 1500)");
            System.out.println("4. Buy hex (cost: 2000)");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (choice == 1 && player2.getCurrentMoney() >= 1000) {
                System.out.println("Enter the x and y coordinates of the hex to place Pandy:");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                scanner.nextLine(); // consume newline
                Hex hex = hexGrid.getHex(x, y);
                if (hex!= null && hex.getOwner() == player2 &&!hex.isOccupied()) {
                    player2.buyMinion(new Pandy(gameService), hex);
                    System.out.println("Pandy placed at (" + x + ", " + y + ")");
                } else {
                    System.out.println("Invalid hex!");
                }
            } else if (choice == 2 && player2.getCurrentMoney() >= 1200) {
                System.out.println("Enter the x and y coordinates of the hex to place Cheesebear:");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                scanner.nextLine(); // consume newline
                Hex hex = hexGrid.getHex(x, y);
                if (hex!= null && hex.getOwner() == player2 &&!hex.isOccupied()) {
                    player2.buyMinion(new Cheesebear(gameService), hex);
                    System.out.println("Cheesebear placed at (" + x + ", " + y + ")");
                } else {
                    System.out.println("Invalid hex!");
                }
            } else if (choice == 3 && player2.getCurrentMoney() >= 1500) {
                System.out.println("Enter the x and y coordinates of the hex to place Moonum:");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                scanner.nextLine(); // consume newline
                Hex hex = hexGrid.getHex(x, y);
                if (hex!= null && hex.getOwner() == player2 &&!hex.isOccupied()) {
                    player2.buyMinion(new Moonum(gameService), hex);
                    System.out.println("Moonum placed at (" + x + ", " + y + ")");
                } else {
                    System.out.println("Invalid hex!");
                }
            } else if (choice == 4 && player2.getCurrentMoney() >= 2000) {
                System.out.println("Enter the x and y coordinates of the hex to buy:");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                scanner.nextLine(); // consume newline
                Hex hex = hexGrid.getHex(x, y);
                if (hex!= null && hex.getOwner() == null && player2.isAdjacentToOwnedHex(hex)) {
                    player2.buyHex(hex);
                    System.out.println("Hex bought at (" + x + ", " + y + ")");
                } else {
                    System.out.println("Invalid hex!");
                }
            } else {
                System.out.println("Not enough money!");
            }
        }
        // ผู้เล่น 2 โจมตี
        for (Minion minion: player2.getMinions()) {
            if (minion.isAlive()) {
                minion.executeStrategy(this, player2);
            }
        }
    }

    public boolean isGameOver() {
        return currentTurn > maxTurns ||!player1.hasMinionsAlive() ||!player2.hasMinionsAlive();
    }

    public Player determineWinner() {
        if (!player1.hasMinionsAlive()) {
            return player2;
        }
        if (!player2.hasMinionsAlive()) {
            return player1;
        }
        int minionCount1 = player1.getMinions().size();
        int minionCount2 = player2.getMinions().size();
        if (minionCount1 > minionCount2) {
            return player1;
        }
        if (minionCount2 > minionCount1) {
            return player2;
        }
        return null; // Draw
    }

    public Hex getHexOfMinion(Minion minion) {
        Hex[][] grid = hexGrid.getGrid(); // get the 2D array
        for (Hex[] row: grid) { // loop through the rows
            for (Hex hex: row) { // loop through the hexes in each row
                if (hex.getMinion() == minion) {
                    return hex;
                }
            }
        }
        return null;
    }
    public Minion findNearestEnemy(Minion minion) {
        List<Minion> allEnemies = new ArrayList<>();
        allEnemies.addAll(player1.getMinions());
        allEnemies.addAll(player2.getMinions());
        allEnemies.remove(minion);

        Minion nearest = null;
        int minDistance = Integer.MAX_VALUE;
        Hex minionHex = getHexOfMinion(minion);

        for (Minion enemy: allEnemies) {
            Hex enemyHex = getHexOfMinion(enemy);
            if (enemyHex!= null) {
                int distance = Math.abs(minionHex.getX() - enemyHex.getX()) + Math.abs(minionHex.getY() - enemyHex.getY());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = enemy;
                }
            }
        }
        return nearest;
    }

    public Hex getNextStepTowards(Hex from, Hex to) {
        List<Hex> adjacentHexes = hexGrid.getAdjacentHexes(from);

        Hex nextStep = null;
        int minDistance = Integer.MAX_VALUE;

        for (Hex hex: adjacentHexes) {
            int distance = Math.abs(hex.getX() - to.getX()) + Math.abs(hex.getY() - to.getY());
            if (distance < minDistance &&!hex.isOccupied()) {
                minDistance = distance;
                nextStep = hex;
            }
        }

        return nextStep;
    }

}