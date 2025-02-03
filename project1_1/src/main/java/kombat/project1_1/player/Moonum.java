package kombat.project1_1.player;

import kombat.project1_1.model.Minion;

public class Moonum extends Minion {
    public Moonum() {
        super(
                "Moonum",              // ชื่อมินเนียน
                15000,                 // ค่าตัว (Cost)
                500,                   // พลังโจมตี (Attack Power)
                400,                   // การป้องกัน (Defense Power)
                2500,                  // Max HP
                900,                   // ค่าการโจมตี (Attack Cost)
                1300,                  // ค่าการป้องกัน (Defense Cost)
                400,                   // ค่าการป้องกัน HP (Defense Protection)
                new MoonumMinionStrategy() // กลยุทธ์เฉพาะของ Moonum
        );
    }
}
