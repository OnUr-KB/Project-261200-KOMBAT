package main;

import minion.MinionStrategyToken;
import model.GameState;
import model.HexGrid;
import model.Minion;
import model.Player;
import minion.MinionStrategyAST;
import minion.MinionStrategyLexer;
import minion.MinionStrategyParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String args) {
        Scanner scanner = new Scanner(System.in);

        // รับชื่อผู้เล่น
        System.out.print("ชื่อผู้เล่น 1: ");
        String player1Name = scanner.nextLine();
        System.out.print("ชื่อผู้เล่น 2: ");
        String player2Name = scanner.nextLine();

        // สร้างผู้เล่น
        Player player1 = new Player(player1Name);
        Player player2 = new Player(player2Name);

        // สร้าง GameState
        GameState gameState = new GameState(player1, player2);

        // รับ input และกำหนดค่า inputDefense, strategy ให้กับผู้เล่นแต่ละคน
        for (Player player: new Player {player1, player2}) {
            System.out.print("ค่า inputDefense ของ " + player.getName() + ": ");
            player.setInputDefense(scanner.nextInt());
            scanner.nextLine(); // consume newline

            System.out.print("กลยุทธ์ของ " + player.getName() + " (strategy1, strategy2, strategy3): ");
            String strategyName = scanner.nextLine();
            MinionStrategyAST.Statement strategy = parseStrategy(strategyName); // TODO: Implement parseStrategy method
            // TODO: กำหนด strategy ให้กับ Minion ของผู้เล่น
        }

        // เมธอดสำหรับแปลงชื่อ strategy เป็น AST
        private static MinionStrategyAST.Statement parseStrategy(String strategyName) {
            String strategyCode = "";
            try {
                // อ่านไฟล์ strategy
                strategyCode = new String(Files.readAllBytes(Paths.get("src/strategy/" + strategyName )));
            } catch (IOException e) {
                System.err.println("Error reading strategy file: " + e.getMessage());
            }

            // Lexing
            MinionStrategyLexer lexer = new MinionStrategyLexer(strategyCode);
            List<MinionStrategyToken> tokens = lexer.lex();

            // Parsing
            MinionStrategyParser parser = new MinionStrategyParser(tokens);
            return parser.parse(); // คืนค่า AST
        }

        // วนลูปเกมจนกว่าจะจบ
        while (!gameState.isGameOver()) {
            // อัพเดทสถานะเกม
            gameState.update();

            // TODO: แสดงผลสถานะเกมในคอนโซล
            //...

            // TODO: รับ input จากผู้เล่นในแต่ละเทิร์น
            //...
        }

        // แสดงผลผู้ชนะ
        Player winner = gameState.getWinner();
        if (winner!= null) {
            System.out.println("ผู้ชนะคือ " + winner.getName() + "!");
        } else {
            System.out.println("เสมอกัน!");
        }

        scanner.close();
    }


}