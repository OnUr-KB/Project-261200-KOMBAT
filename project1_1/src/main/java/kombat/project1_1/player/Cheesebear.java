package kombat.project1_1.player;
import kombat.project1_1.model.Minion;

public class Cheesebear extends Minion {
    public Cheesebear() {
        super(
                "Cheesebear",         // ชื่อมินเนียน
                12000,                // ค่าตัว (Cost)
                900,                  // พลังโจมตี (Attack Power)
                200,                  // การป้องกัน (Defense Power)
                1500,                 // Max HP
                1200,                 // ค่าการโจมตี (Attack Cost)
                1500,                 // ค่าการป้องกัน (Defense Cost)
                200,                  // ค่าการป้องกัน HP (Defense Protection)
                new CheesebearMinionStrategy() // กลยุทธ์เฉพาะของ Cheesebear
        );
    }
}
