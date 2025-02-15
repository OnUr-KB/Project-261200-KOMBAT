package kombat.project1_1.player;


import kombat.project1_1.model.GameState;
import kombat.project1_1.model.Hex;
import kombat.project1_1.model.Minion;
import kombat.project1_1.model.Player;
import kombat.project1_1.service.GameService;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PandyMinionStrategy implements MinionStrategy {

    private final Random random = new Random();
    private GameService gameService;
    public PandyMinionStrategy(GameService gameService) {
        this.gameService = gameService;
    }


    @Override
    public void execute(Minion minion, GameState gameState, Player owner) {
        // ใช้ StrategyEvaluator เพื่อรันโค้ด Strategy
        StrategyEvaluator evaluator = new StrategyEvaluator(minion, gameState, owner);
        evaluator.execute(StrategyParser.parse(getStrategyCode()));
    }
    @Override
    public String getStrategyCode() {
        // ส่งโค้ด Strategy ที่กำหนดไว้ตามสเปค
        return
                """
                t = t + 1  # keeping track of the turn number
                m = 0   # number of random moves this turn
                while (2 - m) {  # made less than 2 random moves
                  if (budget - 300) then {} else done  # too poor to do anything else
                  opponentLoc = opponent
                  if (opponentLoc / 10 - 1) then {  # opponent afar
                    if (opponentLoc % 10 - 5) then move upleft
                    else if (opponentLoc % 10 - 4) then move downleft
                    else if (opponentLoc % 10 - 3) then move down
                    else if (opponentLoc % 10 - 2) then move downright
                    else if (opponentLoc % 10 - 1) then move upright
                    else move up
                  } else if (opponentLoc) then {  # opponent adjacent to this minion
                    if (opponentLoc % 10 - 5) then {
                      cost = 10 ^ (nearby upleft % 100 + 1)
                      if (budget - cost) then shoot upleft cost else {}
                    }
                    else if (opponentLoc % 10 - 4) then {
                      cost = 10 ^ (nearby downleft % 100 + 1)
                      if (budget - cost) then shoot downleft cost else {}
                    }
                    else if (opponentLoc % 10 - 3) then {
                      cost = 10 ^ (nearby down % 100 + 1)
                      if (budget - cost) then shoot down cost else {}
                    }
                    else if (opponentLoc % 10 - 2) then {
                      cost = 10 ^ (nearby downright % 100 + 1)
                      if (budget - cost) then shoot downright cost else {}
                    }
                    else if (opponentLoc % 10 - 1) then {
                      cost = 10 ^ (nearby upright % 100 + 1)
                      if (budget - cost) then shoot upright cost else {}
                    }
                    else {
                      cost = 10 ^ (nearby up % 100 + 1)
                      if (budget - cost) then shoot up cost else {}
                    }
                    # will defend next turn
                    x = owner.inputdefense() * 4 ;
                  } else {  # no visible opponent; move in a random direction
                    try = 0  # keep track of number of attempts
                    while (3 - try) {  # no more than 3 attempts
                      success = 1
                      dir = random % 6
                      # (nearby <dir> % 10 + 1) ^ 2 is positive if adjacent cell in <dir> has no ally
                      if ((dir - 4) * (nearby upleft % 10 + 1) ^ 2) then move upleft
                      else if ((dir - 3) * (nearby downleft % 10 + 1) ^ 2) then move downleft
                      else if ((dir - 2) * (nearby down % 10 + 1) ^ 2) then move down
                      else if ((dir - 1) * (nearby downright % 10 + 1) ^ 2) then move downright
                      else if (dir * (nearby upright % 10 + 1) ^ 2) then move upright
                      else if ((nearby up % 10 + 1) ^ 2) move up
                      else success = 0
                      if (success) then try = 3 else try = try + 1
                    }
                    m = m + 1
                  }
                }  # end while
                
              """;


    }
}
