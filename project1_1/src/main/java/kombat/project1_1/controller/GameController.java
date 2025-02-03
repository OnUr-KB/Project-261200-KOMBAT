//package kombat.project1_1.controller;
//import kombat.project1_1.model.*;
//import kombat.project1_1.player.*;
//import kombat.project1_1.service.GameService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/game")
//public class GameController {
//
//    @Autowired
//    private GameService gameService;
//
//    // เริ่มเกมใหม่
//    @PostMapping("/start")
//    public String startGame(@RequestParam String player1Name, @RequestParam String player2Name, @RequestParam int maxTurns) {
//        Player player1 = new Player(player1Name);
//        Player player2 = new Player(player2Name);
//        gameService.startGame(player1, player2, maxTurns);
//        return "เกมเริ่มต้นเรียบร้อยแล้ว!";
//    }
//
//    // ซื้อ Hex
//    @PostMapping("/buy-hex")
//    public String buyHex(@RequestParam String playerName, @RequestParam int x, @RequestParam int y, @RequestParam int cost) {
//        Player player = gameService.getGameState().getPlayer1().getName().equals(playerName) ? gameService.getGameState().getPlayer1() : gameService.getGameState().getPlayer2();
//        Hex hex = gameService.getGameState().getHexGrid().getHex(x, y);
//        return gameService.buyHex(player, hex, cost) ? "ซื้อ Hex สำเร็จ!" : "ไม่สามารถซื้อ Hex ได้!";
//    }
//
//    // วางมินเนียน
//    @PostMapping("/place-minion")
//    public String placeMinion(@RequestParam String playerName, @RequestParam String minionType, @RequestParam int x, @RequestParam int y) {
//        Player player = gameService.getGameState().getPlayer1().getName().equals(playerName) ? gameService.getGameState().getPlayer1() : gameService.getGameState().getPlayer2();
//        Minion minion = createMinion(minionType);
//        Hex hex = gameService.getGameState().getHexGrid().getHex(x, y);
//        return gameService.placeMinion(player, minion, hex) ? "วางมินเนียนสำเร็จ!" : "ไม่สามารถวางมินเนียนได้!";
//    }
//
//    // โจมตี
//    @PostMapping("/attack")
//    public String attack(@RequestParam String attackerName, @RequestParam String targetName) {
//        Player attacker = gameService.getGameState().getPlayer1().getName().equals(attackerName) ? gameService.getGameState().getPlayer1() : gameService.getGameState().getPlayer2();
//        Minion attackingMinion = findMinionByName(attacker, attackerName);
//        Minion targetMinion = findMinionByName(gameService.getGameState().getPlayer1().equals(attacker) ? gameService.getGameState().getPlayer2() : gameService.getGameState().getPlayer1(), targetName);
//        return gameService.attack(attacker, attackingMinion, targetMinion) ? "โจมตีสำเร็จ!" : "ไม่สามารถโจมตีได้!";
//    }
//
//    // จบเทิร์น
//    @PostMapping("/end-turn")
//    public String endTurn() {
//        gameService.endTurn();
//        return gameService.isGameOver() ? "เกมจบแล้ว! ผู้ชนะคือ: " + gameService.determineWinner().getName() : "จบเทิร์นเรียบร้อย!";
//    }
//
//    // Helper Method สำหรับสร้างมินเนียน
//    private Minion createMinion(String minionType) {
//        return switch (minionType) {
//            case "Pandy" -> new Pandy();
//            case "Cheesebear" -> new Cheesebear();
//            case "Moonum" -> new Moonum();
//            default -> throw new IllegalArgumentException("ไม่พบประเภทของมินเนียนนี้!");
//        };
//    }
//
//    // Helper Method สำหรับหามินเนียนตามชื่อ
//    private Minion findMinionByName(Player player, String minionName) {
//        return player.getMinions().stream()
//                .filter(minion -> minion.getName().equals(minionName))
//                .findFirst()
//                .orElse(null);
//    }
//}
