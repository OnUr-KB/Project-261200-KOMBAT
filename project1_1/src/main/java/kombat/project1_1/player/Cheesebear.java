package kombat.project1_1.player;
import kombat.project1_1.model.Minion;
import kombat.project1_1.service.GameService;

public class Cheesebear extends Minion {
    private static final int getInputDefense = ;

    public Cheesebear(GameService gameService) {
        super(
                "Cheesebear",         // ชื่อมินเนียน
                1200,                // ค่าตัว (Cost)
                900,                  // พลังโจมตี (Attack Power)
                getInputDefense,                  // การป้องกัน (Defense Power)
                3000,                 // Max HP
                1200,                 // ค่าการโจมตี (Attack Cost)
                1500,                 // ค่าการป้องกัน (Defense Cost)
                gameService,
                new CheesebearMinionStrategy(gameService) // กลยุทธ์เฉพาะของ Cheesebear

        );
    }
}