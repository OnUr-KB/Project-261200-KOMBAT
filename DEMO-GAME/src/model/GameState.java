package model;

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