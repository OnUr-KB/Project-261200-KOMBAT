package kombat.project1_1.player;

import kombat.project1_1.model.Minion;
import kombat.project1_1.service.GameService;

public class Moonum extends Minion {
    public Moonum(GameService gameService) {
        super(
                "Moonum",              // ชื่อมินเนียน
                1500,                 // ค่าตัว (Cost)
                500,                   // พลังโจมตี (Attack Power)
                400,                   // การป้องกัน (Defense Power)
                7000,                  // Max HP
                900,                   // ค่าการโจมตี (Attack Cost)
                1300,                  // ค่าการป้องกัน (Defense Cost)
                gameService,
                new MoonumMinionStrategy(gameService) // กลยุทธ์เฉพาะของ Moonum

        );
    }
}