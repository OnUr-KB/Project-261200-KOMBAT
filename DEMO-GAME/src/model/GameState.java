/*package model;

import java.util.List;

public class GameState {
    private Player player1; // ผู้เล่น 1
    private Player player2; // ผู้เล่น 2
    private HexGrid hexGrid; // HexGrid ของเกม
    private int currentTurn; // เทิร์นปัจจุบัน

    public GameState(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.hexGrid = new HexGrid(8, 8); // สร้าง HexGrid ขนาด 8x8
        this.currentTurn = 1; // เริ่มต้นที่เทิร์น 1
    }

    private Player getCurrentPlayer() {
        if (currentTurn % 2 == 1) {
            return player1; // คืนค่า player1 ถ้าเป็นเทิร์นเลขคี่
        } else {
            return player2; // คืนค่า player2 ถ้าเป็นเทิร์นเลขคู่
        }
    }

    public int getBudget() {
        return getCurrentPlayer().getBudget(); // คืนค่า budget ของผู้เล่นปัจจุบัน
    }

    public int getInputDefense() {
        return getCurrentPlayer().getInputDefense(); // คืนค่า inputDefense ของผู้เล่นปัจจุบัน
    }

    public void move(String direction) {
        // หา Minion ปัจจุบัน
        List<Minion> currentPlayerMinions = getCurrentPlayer().getMinions();
        Minion currentMinion = null;
        for (Minion minion: currentPlayerMinions) {
            if (minion.getHex()!= null) { // เลือก Minion ตัวแรกที่อยู่ใน HexGrid
                currentMinion = minion;
                break;
            }
        }

        // หา Hex ปัจจุบันของ Minion
        Hex currentHex = currentMinion.getHex();

        // หา Hex เป้าหมาย
        Hex targetHex = null;
        switch (direction) {
            case "up":
                targetHex = hexGrid.getHex(currentHex.getRow() - 1, currentHex.getCol());
                break;
            case "down":
                targetHex = hexGrid.getHex(currentHex.getRow() + 1, currentHex.getCol());
                break;
            case "upleft":
                targetHex = hexGrid.getHex(currentHex.getRow() - 1, currentHex.getCol() - 1);
                break;
            case "upright":
                targetHex = hexGrid.getHex(currentHex.getRow() - 1, currentHex.getCol() + 1);
                break;
            case "downleft":
                targetHex = hexGrid.getHex(currentHex.getRow() + 1, currentHex.getCol() - 1);
                break;
            case "downright":
                targetHex = hexGrid.getHex(currentHex.getRow() + 1, currentHex.getCol() + 1);
                break;
        }

        // ตรวจสอบว่า Hex เป้าหมาย valid
        if (targetHex!= null && targetHex.getOwner() == getCurrentPlayer() && targetHex.getMinion() == null) {
            // เคลื่อนย้าย Minion
            currentMinion.move(direction, hexGrid);

            // ลด budget
            getCurrentPlayer().setBudget(getCurrentPlayer().getBudget() - 1);
        }
    }

    public void shoot(String direction, long expenditure) {
        // หา Minion ปัจจุบัน
        Minion currentMinion = getCurrentPlayer().getMinions().get(0); // TODO: Implement logic for selecting the current minion

        // หา Hex ปัจจุบันของ Minion
        Hex currentHex = currentMinion.getHex();

        // หา Hex เป้าหมาย
        Hex targetHex = null;
        switch (direction) {
            case "up":
                targetHex = hexGrid.getHex(currentHex.getRow() - 1, currentHex.getCol());
                break;
            case "down":
                targetHex = hexGrid.getHex(currentHex.getRow() + 1, currentHex.getCol());
                break;
            case "upleft":
                targetHex = hexGrid.getHex(currentHex.getRow() - 1, currentHex.getCol() - 1);
                break;
            case "upright":
                targetHex = hexGrid.getHex(currentHex.getRow() - 1, currentHex.getCol() + 1);
                break;
            case "downleft":
                targetHex = hexGrid.getHex(currentHex.getRow() + 1, currentHex.getCol() - 1);
                break;
            case "downright":
                targetHex = hexGrid.getHex(currentHex.getRow() + 1, currentHex.getCol() + 1);
                break;
        }

        // ตรวจสอบว่า Hex เป้าหมาย valid  และมี Minion  ของฝ่ายตรงข้ามอยู่
        if (targetHex!= null && targetHex.getMinion()!= null && targetHex.getMinion().getOwner()!= getCurrentPlayer()) {
            // โจมตี Minion  เป้าหมาย
            currentMinion.attack(targetHex.getMinion(), (int) expenditure); // TODO: Implement attack method in Minion class

            // ลด budget
            getCurrentPlayer().setBudget(getCurrentPlayer().getBudget() - expenditure - 1);
        }
    }


    public int getAlly() {
        // หา Minion ปัจจุบัน
        Minion currentMinion = getCurrentPlayer().getMinions().get(0); // TODO: Implement logic for selecting the current minion

        // หา Hex ปัจจุบันของ Minion
        Hex currentHex = currentMinion.getHex();

        // หา Ally ที่ใกล้ที่สุด
        Hex nearestAllyHex = null;
        int minDistance = Integer.MAX_VALUE;
        for (Minion minion: getCurrentPlayer().getMinions()) {
            if (minion!= currentMinion && minion.getHex()!= null) { // ไม่นับ Minion ปัจจุบันและ Minion ที่ไม่ได้อยู่ใน HexGrid
                Hex minionHex = minion.getHex();
                int distance = calculateDistance(currentHex, minionHex); // TODO: Implement calculateDistance method
                if (distance < minDistance) {
                    nearestAllyHex = minionHex;
                    minDistance = distance;
                }
            }
        }

        // คืนค่า location ของ Ally ที่ใกล้ที่สุด
        if (nearestAllyHex!= null) {
            return getLocation(nearestAllyHex); // TODO: Implement getLocation method
        } else {
            return 0; // ไม่มี Ally
        }
    }

    // เมธอดสำหรับคำนวณระยะห่างระหว่าง Hex 2 ช่อง
    private int calculateDistance(Hex hex1, Hex hex2) {
        int rowDiff = Math.abs(hex1.getRow() - hex2.getRow());
        int colDiff = Math.abs(hex1.getCol() - hex2.getCol());
        return Math.max(rowDiff, colDiff); // คืนค่าระยะห่างสูงสุดระหว่าง rowDiff และ colDiff
    }


    // เมธอดสำหรับแปลง Hex เป็น location ในรูปแบบ integer
    private int getLocation(Hex hex) {
        // หา Minion ปัจจุบัน
        Minion currentMinion = getCurrentPlayer().getMinions().get(0); // TODO: Implement logic for selecting the current minion

        // หา Hex ปัจจุบันของ Minion
        Hex currentHex = currentMinion.getHex();

        // คำนวณ rowDiff และ colDiff
        int rowDiff = hex.getRow() - currentHex.getRow();
        int colDiff = hex.getCol() - currentHex.getCol();

        // แปลงเป็น location ตาม diagram
        if (rowDiff == 0 && colDiff == 0) {
            return 0; // Hex ปัจจุบัน
        } else if (rowDiff < 0 && colDiff == 0) {
            return 11 + Math.abs(rowDiff) - 1; // Up
        } else if (rowDiff < 0 && colDiff > 0) {
            return 21 + Math.max(Math.abs(rowDiff), colDiff) - 1; // Upright
        } else if (rowDiff == 0 && colDiff > 0) {
            return 31 + colDiff - 1; // Right
        } else if (rowDiff > 0 && colDiff > 0) {
            return 41 + Math.max(rowDiff, colDiff) - 1; // Downright
        } else if (rowDiff > 0 && colDiff == 0) {
            return 36 + rowDiff - 1; // Down
        } else if (rowDiff > 0 && colDiff < 0) {
            return 26 + Math.max(rowDiff, Math.abs(colDiff)) - 1; // Downleft
        } else if (rowDiff == 0 && colDiff < 0) {
            return 16 + Math.abs(colDiff) - 1; // Left
        } else { // rowDiff < 0 && colDiff < 0
            return 11 + Math.max(Math.abs(rowDiff), Math.abs(colDiff)) - 1; // Upleft
        }
    }


    public int getOpponent() {
        // หา Minion ปัจจุบัน
        Minion currentMinion = getCurrentPlayer().getMinions().get(0); // TODO: Implement logic for selecting the current minion

        // หา Hex ปัจจุบันของ Minion
        Hex currentHex = currentMinion.getHex();

        // หา Opponent ที่ใกล้ที่สุด
        Hex nearestOpponentHex = null;
        int minDistance = Integer.MAX_VALUE;
        for (Minion minion: getOpponentPlayer().getMinions()) { // วนลูป Minion ของผู้เล่นฝ่ายตรงข้าม
            if (minion.getHex()!= null) { // ไม่นับ Minion ที่ไม่ได้อยู่ใน HexGrid
                Hex minionHex = minion.getHex();
                int distance = calculateDistance(currentHex, minionHex);
                if (distance < minDistance) {
                    nearestOpponentHex = minionHex;
                    minDistance = distance;
                }
            }
        }

        // คืนค่า location ของ Opponent ที่ใกล้ที่สุด
        if (nearestOpponentHex!= null) {
            return getLocation(nearestOpponentHex);
        } else {
            return 0; // ไม่มี Opponent
        }
    }

    // เมธอดสำหรับหาผู้เล่นฝ่ายตรงข้าม
    private Player getOpponentPlayer() {
        if (getCurrentPlayer() == player1) {
            return player2; // คืนค่า player2 ถ้าผู้เล่นปัจจุบันคือ player1
        } else {
            return player1; // คืนค่า player1 ถ้าผู้เล่นปัจจุบันคือ player2
        }
    }

    public int getNearby(String direction) {
        // หา Minion ปัจจุบัน
        Minion currentMinion = getCurrentPlayer().getMinions().get(0); // TODO: Implement logic for selecting the current minion

        // หา Hex ปัจจุบันของ Minion
        Hex currentHex = currentMinion.getHex();

        // หา Minion ที่ใกล้ที่สุดในทิศทางที่ระบุ
        Hex targetHex = null;
        switch (direction) {
            case "up":
                targetHex = hexGrid.getHex(currentHex.getRow() - 1, currentHex.getCol());
                break;
            case "down":
                targetHex = hexGrid.getHex(currentHex.getRow() + 1, currentHex.getCol());
                break;
            //... (handle other directions: upleft, upright, downleft, downright)
        }

        // ตรวจสอบว่า targetHex มี Minion อยู่
        if (targetHex!= null && targetHex.getMinion()!= null) {
            Minion nearbyMinion = targetHex.getMinion();
            int x = String.valueOf(nearbyMinion.getHp()).length(); // จำนวนหลักของ HP
            int y = String.valueOf(nearbyMinion.getDefense()).length(); // จำนวนหลักของ defense
            int z = calculateDistance(currentHex, targetHex); // ระยะห่าง

            if (nearbyMinion.getOwner() == getCurrentPlayer()) { // Ally
                return -(100 * x + 10 * y + z); // คืนค่าติดลบ
            } else { // Opponent
                return 100 * x + 10 * y + z;
            }
        } else {
            return 0; // ไม่มี Minion
        }
    }








    // เมธอดสำหรับอัพเดทสถานะของเกมในแต่ละเทิร์น
    public void update() {
        // TODO: 1. คำนวณดอกเบี้ยและอัพเดทงบประมาณของผู้เล่น
        // TODO: 2. ให้ผู้เล่นแต่ละคนดำเนินการในเทิร์น (ซื้อ Minion, ซื้อ Hex, โจมตี)
        // TODO: 3. อัพเดทสถานะของ Minion และ Hex
        // TODO: 4. เพิ่ม currentTurn
    }

    // เมธอดสำหรับตรวจสอบว่าเกมจบลงหรือไม่
    public boolean isGameOver() {
        // TODO: ตรวจสอบเงื่อนไขการจบเกม (ครบจำนวนเทิร์น, ฝ่ายใดฝ่ายหนึ่งไม่มี Minion เหลือ)
        return false;
    }

    // เมธอดสำหรับหาผู้ชนะ
    public Player getWinner() {
        // TODO: หาผู้ชนะตามเงื่อนไขการจบเกม
        return null;
    }
}

 */
package main.model;

import java.util.List;
import config.Config; // Import Config
import java.util.Map;
import java.util.HashMap;


public class GameState {
    private Player player1; // ผู้เล่นคนที่ 1
    private Player player2; // ผู้เล่นคนที่ 2
    private HexGrid hexGrid; // ตาราง Hex ของเกม
    private int currentTurn; // เทิร์นปัจจุบัน
    private Map<String, Long> variables; // ตัวแปร (ใช้สำหรับ Minion strategies)


    public GameState(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.hexGrid = new HexGrid(8, 8); // สร้าง HexGrid ขนาด 8x8
        this.currentTurn = 1; // เริ่มต้นที่เทิร์น 1
        this.variables = new HashMap<>(); // เริ่มต้นตัวแปร
    }
    public HexGrid getHexGrid(){
        return this.hexGrid; // คืนค่า HexGrid
    }

    public Player getPlayer1(){
        return this.player1; // คืนค่า player1
    }
    public Player getPlayer2(){
        return this.player2; // คืนค่า player2
    }

    public int getCurrentTurn(){
        return this.currentTurn;  //คืนค่า turn ปัจจุบัน
    }


    private Player getCurrentPlayer() {
        // คืนค่าผู้เล่นปัจจุบัน (player1 ถ้าเป็นเทิร์นเลขคี่, player2 ถ้าเลขคู่)
        return (currentTurn % 2 == 1) ? player1 : player2;
    }

    public int getBudget() {
        return getCurrentPlayer().getBudget(); // คืนค่า budget ของผู้เล่นปัจจุบัน
    }

    public int getInputDefense() {
        return getCurrentPlayer().getInputDefense(); // คืนค่า inputDefense ของผู้เล่นปัจจุบัน
    }
    public void move(String direction) {
        // หา Minion ปัจจุบันของผู้เล่นปัจจุบันที่อยู่ใน HexGrid
        List<Minion> currentPlayerMinions = getCurrentPlayer().getMinions();
        Minion currentMinion = null;
        for (Minion minion : currentPlayerMinions) {
            if (minion.getHex() != null) { // ตรวจสอบว่า Minion อยู่บน HexGrid หรือไม่
                currentMinion = minion;
                break; // พบ Minion ตัวแรกที่อยู่บน HexGrid แล้วออกจาก loop
            }
        }

        if(currentMinion == null) return; // ถ้าไม่มี Minion บน HexGrid ก็ไม่ต้องทำอะไร

        // หา Hex ปัจจุบันของ Minion
        Hex currentHex = currentMinion.getHex();

        // หา Hex เป้าหมายตามทิศทางที่ระบุ
        Hex targetHex = getTargetHex(currentHex, direction);

        // ตรวจสอบว่า Hex เป้าหมาย valid (อยู่ในขอบเขต, เป็นของผู้เล่นปัจจุบัน, และไม่มี Minion อื่นอยู่)
        if (targetHex != null && targetHex.getOwner() == getCurrentPlayer() && targetHex.getMinion() == null) {
            // เคลื่อนย้าย Minion
            currentMinion.move(direction, hexGrid);

        }
    }


    public void shoot(String direction, long expenditure) {
        // หา Minion ปัจจุบันของผู้เล่นปัจจุบันที่อยู่ใน HexGrid
        List<Minion> currentPlayerMinions = getCurrentPlayer().getMinions();
        Minion currentMinion = null;
        for (Minion minion : currentPlayerMinions) {
            if (minion.getHex() != null) { // ตรวจสอบว่า Minion อยู่บน HexGrid หรือไม่
                currentMinion = minion;
                break; // พบ Minion ตัวแรกที่อยู่บน HexGrid แล้วออกจาก loop
            }
        }

        if(currentMinion == null) return; // ถ้าไม่มี Minion บน HexGrid ก็ไม่ต้องทำอะไร

        // หา Hex ปัจจุบันของ Minion
        Hex currentHex = currentMinion.getHex();

        // หา Hex เป้าหมายตามทิศทางที่ระบุ
        Hex targetHex = getTargetHex(currentHex, direction);

        // ตรวจสอบว่า Hex เป้าหมาย valid และมี Minion ของฝ่ายตรงข้ามอยู่
        if (targetHex != null && targetHex.getMinion() != null && targetHex.getMinion().getOwner() != getCurrentPlayer()) {
            // โจมตี Minion เป้าหมาย
            currentMinion.attack(targetHex.getMinion(), (int) expenditure);
        }
    }


    public int getAlly() {
        // หา Minion ปัจจุบัน
        List<Minion> currentPlayerMinions = getCurrentPlayer().getMinions();
        Minion currentMinion = null;
        for (Minion minion : currentPlayerMinions) {
            if (minion.getHex() != null) {
                currentMinion = minion;
                break;
            }
        }

        if(currentMinion == null){
            return 0; // ไม่มี Minion ใน HexGrid
        }
        // หา Hex ปัจจุบันของ Minion
        Hex currentHex = currentMinion.getHex();

        // หา Ally ที่ใกล้ที่สุด
        Hex nearestAllyHex = null;
        int minDistance = Integer.MAX_VALUE;
        for (Minion minion : getCurrentPlayer().getMinions()) {
            if (minion != currentMinion && minion.getHex() != null) { // ไม่นับ Minion ปัจจุบันและ Minion ที่ไม่ได้อยู่ใน HexGrid
                Hex minionHex = minion.getHex();
                int distance = calculateDistance(currentHex, minionHex); // คำนวณระยะห่าง
                if (distance < minDistance) {
                    nearestAllyHex = minionHex;
                    minDistance = distance;
                }
            }
        }

        // คืนค่า location ของ Ally ที่ใกล้ที่สุด
        if (nearestAllyHex != null) {
            return getLocation(nearestAllyHex, currentHex); // แปลง Hex เป็น location
        } else {
            return 0; // ไม่มี Ally
        }
    }

    // เมธอดสำหรับคำนวณระยะห่างระหว่าง Hex 2 ช่อง
    private int calculateDistance(Hex hex1, Hex hex2) {
        int rowDiff = Math.abs(hex1.getRow() - hex2.getRow()); // ผลต่างของแถว
        int colDiff = Math.abs(hex1.getCol() - hex2.getCol()); // ผลต่างของคอลัมน์
        return Math.max(rowDiff, colDiff); // คืนค่าระยะห่างสูงสุดระหว่าง rowDiff และ colDiff
    }

    private int getLocation(Hex targetHex, Hex currentHex) {
        // คำนวณ rowDiff และ colDiff
        int rowDiff = targetHex.getRow() - currentHex.getRow();
        int colDiff = targetHex.getCol() - currentHex.getCol();

        // แปลงเป็น location ตาม diagram
        if (rowDiff == 0 && colDiff == 0) {
            return 0; // Hex ปัจจุบัน
        } else if (rowDiff < 0 && colDiff == 0) {
            return 11 + Math.abs(rowDiff) - 1; // Up
        } else if (rowDiff < 0 && colDiff > 0) {
            return 21 + Math.max(Math.abs(rowDiff), colDiff) - 1; // Upright
        } else if (rowDiff == 0 && colDiff > 0) {
            return 31 + colDiff - 1; // Right
        } else if (rowDiff > 0 && colDiff > 0) {
            return 41 + Math.max(rowDiff, colDiff) - 1; // Downright
        } else if (rowDiff > 0 && colDiff == 0) {
            return 36 + rowDiff - 1; // Down
        } else if (rowDiff > 0 && colDiff < 0) {
            return 26 + Math.max(rowDiff, Math.abs(colDiff)) - 1; // Downleft
        } else if (rowDiff == 0 && colDiff < 0) {
            return 16 + Math.abs(colDiff) - 1; // Left
        } else { // rowDiff < 0 && colDiff < 0
            return 6 + Math.max(Math.abs(rowDiff), Math.abs(colDiff)); // Upleft
        }
    }


    public int getOpponent() {
        // หา Minion ปัจจุบัน
        List<Minion> currentPlayerMinions = getCurrentPlayer().getMinions();
        Minion currentMinion = null;
        for (Minion minion : currentPlayerMinions) {
            if (minion.getHex() != null) {
                currentMinion = minion;
                break;
            }
        }
        if(currentMinion == null) return 0;

        // หา Hex ปัจจุบันของ Minion
        Hex currentHex = currentMinion.getHex();

        // หา Opponent (ศัตรู) ที่ใกล้ที่สุด
        Hex nearestOpponentHex = null;
        int minDistance = Integer.MAX_VALUE;
        for (Minion minion : getOpponentPlayer().getMinions()) { // วนลูป Minion ของผู้เล่นฝ่ายตรงข้าม
            if (minion.getHex() != null) { // ไม่นับ Minion ที่ไม่ได้อยู่ใน HexGrid
                Hex minionHex = minion.getHex();
                int distance = calculateDistance(currentHex, minionHex); // คำนวณระยะห่าง
                if (distance < minDistance) {
                    nearestOpponentHex = minionHex;
                    minDistance = distance;
                }
            }
        }

        // คืนค่า location ของ Opponent ที่ใกล้ที่สุด
        if (nearestOpponentHex != null) {
            return getLocation(nearestOpponentHex, currentHex);  // แปลง Hex เป็น location
        } else {
            return 0; // ไม่มี Opponent
        }
    }

    // เมธอดสำหรับหาผู้เล่นฝ่ายตรงข้าม
    private Player getOpponentPlayer() {
        return (getCurrentPlayer() == player1) ? player2 : player1;
    }

    public int getNearby(String direction) {
        // หา Minion ปัจจุบัน
        List<Minion> currentPlayerMinions = getCurrentPlayer().getMinions();
        Minion currentMinion = null;
        for (Minion minion : currentPlayerMinions) {
            if (minion.getHex() != null) {
                currentMinion = minion;
                break;
            }
        }

        if(currentMinion == null) return 0;

        // หา Hex ปัจจุบันของ Minion
        Hex currentHex = currentMinion.getHex();

        // หา Hex ตามทิศที่กำหนด
        Hex targetHex = getTargetHex(currentHex, direction);

        // ตรวจสอบว่า targetHex มี Minion อยู่
        if (targetHex != null && targetHex.getMinion() != null) {
            Minion nearbyMinion = targetHex.getMinion();
            int x = String.valueOf(nearbyMinion.getHp()).length(); // จำนวนหลักของ HP
            int y = String.valueOf(nearbyMinion.getDefense()).length(); // จำนวนหลักของ defense
            int z = calculateDistance(currentHex, targetHex); // ระยะห่าง

            if (nearbyMinion.getOwner() == getCurrentPlayer()) { // Ally (เพื่อนร่วมทีม)
                return -(100 * x + 10 * y + z); // คืนค่าติดลบ
            } else { // Opponent (ศัตรู)
                return 100 * x + 10 * y + z; // คืนค่าบวก
            }
        } else {
            return 0; // ไม่มี Minion
        }
    }

    // หา Hex เป้าหมายตามทิศทาง
    private Hex getTargetHex(Hex currentHex, String direction) {
        int row = currentHex.getRow(); // แถวปัจจุบัน
        int col = currentHex.getCol(); // คอลัมน์ปัจจุบัน

        switch (direction.toLowerCase()) {
            case "up":
                return hexGrid.getHex(row - 1, col);
            case "down":
                return hexGrid.getHex(row + 1, col);
            case "upleft":
                return hexGrid.getHex(row - 1, col - (row % 2 == 0 ? 0 : 1)); // เช็คว่าเป็น even row หรือ odd row
            case "upright":
                return hexGrid.getHex(row - 1, col + (row % 2 == 0 ? 1 : 0)); // เช็คว่าเป็น even row หรือ odd row
            case "downleft":
                return hexGrid.getHex(row + 1, col - (row % 2 == 0 ? 0 : 1)); // เช็คว่าเป็น even row หรือ odd row
            case "downright":
                return hexGrid.getHex(row + 1, col + (row % 2 == 0 ? 1 : 0)); // เช็คว่าเป็น even row หรือ odd row
            default:
                return null;
        }
    }

    // อัพเดทสถานะของเกมในแต่ละเทิร์น
    public void update() {


        // 1. คำนวณดอกเบี้ยและอัพเดทงบประมาณของผู้เล่น + เงินประจำเทิร์น
        player1.addBudget((int)(player1.getBudget() * Config.INTEREST_PCT) + Config.TURN_BUDGET);
        player2.addBudget((int)(player2.getBudget() * Config.INTEREST_PCT) + Config.TURN_BUDGET);

        // จำกัด Budget ไม่ให้เกิน
        player1.setBudget(Math.min(player1.getBudget(),Config.MAX_BUDGET ));
        player2.setBudget(Math.min(player2.getBudget(),Config.MAX_BUDGET ));


        // 2.  Reset action points, Execute strategies ของ Minions *ทั้งหมด* ของ player1
        for(Minion minion : player1.getMinions()){
            if(minion.getHex() != null){ // ตรวจสอบให้แน่ใจว่า Minion อยู่บน grid
                minion.resetActionPoints();
            }
        }
        player1.executeMinionStrategies(hexGrid, this); // เรียก strategies


        // 3.  Reset action points, Execute strategies ของ Minions *ทั้งหมด* ของ player2
        for(Minion minion : player2.getMinions()){
            if(minion.getHex() != null){  // ตรวจสอบให้แน่ใจว่า Minion อยู่บน grid
                minion.resetActionPoints();
            }
        }
        player2.executeMinionStrategies(hexGrid, this); // เรียก strategies

        // 4. เพิ่ม currentTurn (ทำหลังสุด เพื่อให้ player1 เริ่มก่อน)
        currentTurn++;
    }

    // ตรวจสอบว่าเกมจบลงหรือไม่
    public boolean isGameOver() {
        // เงื่อนไข 1: ครบจำนวนเทิร์นสูงสุด
        if (currentTurn > Config.MAX_TURNS) {
            return true;
        }


        // เงื่อนไข 2: ผู้เล่นคนใดคนหนึ่งไม่มี Minion เหลืออยู่บน HexGrid
        boolean player1HasMinions = false;
        for(Minion minion : player1.getMinions()){
            if(minion.getHex() != null){ // ตรวจสอบว่า Minion ยังอยู่บน grid หรือไม่
                player1HasMinions = true;
                break;
            }
        }

        boolean player2HasMinions = false;
        for(Minion minion : player2.getMinions()){
            if(minion.getHex() != null){  // ตรวจสอบว่า Minion ยังอยู่บน grid หรือไม่
                player2HasMinions = true;
                break;
            }
        }

        return !player1HasMinions || !player2HasMinions;
    }

    // หาผู้ชนะ
    public Player getWinner() {

        if(isGameOver()){
            // เงื่อนไข 1: ถ้าเกมจบเพราะครบจำนวนเทิร์นสูงสุด
            if (currentTurn > Config.MAX_TURNS) {
                // นับจำนวน Hex ที่แต่ละฝ่ายครอบครอง
                int player1HexCount = 0;
                int player2HexCount = 0;

                for(int r = 0; r < hexGrid.getNumRows(); r++){
                    for(int c = 0; c < hexGrid.getNumCols(); c++){
                        Hex hex = hexGrid.getHex(r, c);
                        if(hex.getOwner() == player1){
                            player1HexCount++;
                        } else if (hex.getOwner() == player2){
                            player2HexCount++;
                        }
                    }
                }
                // ผู้เล่นที่มี Hex มากกว่าชนะ, ถ้าเท่ากันให้ถือว่าเสมอ (คืนค่า null)
                if(player1HexCount > player2HexCount){
                    return player1; // player1 ชนะ
                } else if (player2HexCount > player1HexCount){
                    return player2; // player2 ชนะ
                } else {
                    return null; // เสมอ
                }
            }

            // เงื่อนไข 2: ถ้าจบเพราะ Minion หมด, ผู้เล่นที่ยังมี Minion อยู่ใน HexGrid ชนะ
            boolean player1HasMinions = false;
            for(Minion minion : player1.getMinions()){
                if(minion.getHex() != null){ //check ว่ายังมี Minion บน Grid
                    player1HasMinions = true;
                    break;
                }

            }

            boolean player2HasMinions = false;
            for(Minion minion : player2.getMinions()){
                if(minion.getHex() != null){  //check ว่ายังมี Minion บน Grid
                    player2HasMinions = true;
                    break;
                }
            }

            if(player1HasMinions){
                return player1; // player1 ชนะ
            } else if (player2HasMinions){
                return player2; // player2 ชนะ
            }
        }
        return null; //  เกมยังไม่จบ
    }
    @Override
    public String toString() {
        return "variables=" + variables; // แสดงค่าตัวแปร (ใช้ตอน debug)
    }

    public void setVariables(Map<String, Long> variables) {
        this.variables = variables; // กำหนดค่าตัวแปร
    }

    public Map<String, Long> getVariables() {
        return this.variables;  // คืนค่าตัวแปร
    }

}