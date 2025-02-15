package project.kombat1;

import project.kombat1.minion.*;
import project.kombat1.model.GameState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String args) throws IOException {
        String strategy1 = new String(Files.readAllBytes(Paths.get("strategy1")));
        String strategy2 = new String(Files.readAllBytes(Paths.get("strategy2")));
        String strategy3 = new String(Files.readAllBytes(Paths.get("strategy3")));

        List<MinionStrategyToken> tokens1 = new MinionStrategyLexer(strategy1).lex();
        List<MinionStrategyToken> tokens2 = new MinionStrategyLexer(strategy2).lex();
        List<MinionStrategyToken> tokens3 = new MinionStrategyLexer(strategy3).lex();

        MinionStrategyAST.Statement ast1 = new MinionStrategyParser(tokens1).parse();
        MinionStrategyAST.Statement ast2 = new MinionStrategyParser(tokens2).parse();
        MinionStrategyAST.Statement ast3 = new MinionStrategyParser(tokens3).parse();

        GameState gameState = new GameState();
        MinionStrategyEvaluator evaluator = new MinionStrategyEvaluator(gameState);

        evaluator.evaluate(ast1);
        evaluator.evaluate(ast2);
        evaluator.evaluate(ast3);
    }
}