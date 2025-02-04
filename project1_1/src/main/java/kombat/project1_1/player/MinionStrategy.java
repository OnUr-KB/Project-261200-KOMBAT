package kombat.project1_1.player;

import kombat.project1_1.model.GameState;
import kombat.project1_1.model.Minion;
import kombat.project1_1.model.Player;

public interface MinionStrategy {
    void execute(Minion minion, GameState gameState, Player owner);
    String getStrategyCode();
}