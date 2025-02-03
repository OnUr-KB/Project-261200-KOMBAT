package kombat.project1_1.player;

import kombat.project1_1.model.Minion;

public class Pandy extends Minion {
    public Pandy() {
        super(
                "Ppandy",          // ชื่อมินเนียน
                10000,             // ค่าตัว (Cost)
                700,               // พลังโจมตี (Attack Power)
                300,               // การป้องกัน (Defense Power)
                2000,              // Max HP
                1000,              // ค่าการโจมตี (Attack Cost)
                1200,              // ค่าการป้องกัน (Defense Cost)
                300,               // ค่าการป้องกัน HP (Defense Protection)
                new PandyMinionStrategy() // กลยุทธ์เฉพาะของ Ppandy
        );
    }
}
