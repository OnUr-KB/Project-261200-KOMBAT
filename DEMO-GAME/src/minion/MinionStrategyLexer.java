package minion;


import minion.MinionStrategyToken;

import java.util.List;
import java.util.ArrayList;

public class MinionStrategyLexer {

    private final String input;
    private int position;

    public MinionStrategyLexer(String input) {
        this.input = input;
        this.position = 0;
    }

    public List<MinionStrategyToken> lex() {
        List<MinionStrategyToken> tokens = new ArrayList<>();
        while (position < input.length()) {
            char current = input.charAt(position);
            if (Character.isWhitespace(current)) {
                position++;
            } else if (Character.isDigit(current)) {
                tokens.add(lexNumber());
            } else if (Character.isLetter(current)) {
                tokens.add(lexIdentifier());
            } else if (current == '=') {
                tokens.add(new MinionStrategyToken(MinionStrategyToken.Type.ASSIGN, "="));
                position++;
            } else if (current == '+') {
                tokens.add(new MinionStrategyToken(MinionStrategyToken.Type.PLUS, "+"));
                position++;
            } else if (current == '-') {
                tokens.add(new MinionStrategyToken(MinionStrategyToken.Type.MINUS, "-"));
                position++;
            } else if (current == '*') {
                tokens.add(new MinionStrategyToken(MinionStrategyToken.Type.MULTIPLY, "*"));
                position++;
            } else if (current == '/') {
                tokens.add(new MinionStrategyToken(MinionStrategyToken.Type.DIVIDE, "/"));
                position++;
            } else if (current == '%') {
                tokens.add(new MinionStrategyToken(MinionStrategyToken.Type.MODULO, "%"));
                position++;
            } else if (current == '(') {
                tokens.add(new MinionStrategyToken(MinionStrategyToken.Type.LPAREN, "("));
                position++;
            } else if (current == ')') {
                tokens.add(new MinionStrategyToken(MinionStrategyToken.Type.RPAREN, ")"));
                position++;
            } else if (current == '{') {
                tokens.add(new MinionStrategyToken(MinionStrategyToken.Type.LBRACE, "{"));
                position++;
            } else if (current == '}') {
                tokens.add(new MinionStrategyToken(MinionStrategyToken.Type.RBRACE, "}"));
                position++;
            } else if (current == '$') {
                tokens.add(new MinionStrategyToken(MinionStrategyToken.Type.DOLLAR, "$"));
                position++;
            } else if (current == '^') {
                tokens.add(new MinionStrategyToken(MinionStrategyToken.Type.POWER, "^"));
                position++;
            } else {
                throw new RuntimeException("Unexpected character: " + current);
            }
        }
        return tokens;
    }

    private MinionStrategyToken lexNumber() {
        int start = position;
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            position++;
        }
        return new MinionStrategyToken(MinionStrategyToken.Type.NUMBER, input.substring(start, position));
    }

    private MinionStrategyToken lexIdentifier() {
        int start = position;
        while (position < input.length() && Character.isLetterOrDigit(input.charAt(position))) {
            position++;
        }
        String identifier = input.substring(start, position);
        switch (identifier) {
            case "if":
                return new MinionStrategyToken(MinionStrategyToken.Type.IF, identifier);
            case "then":
                return new MinionStrategyToken(MinionStrategyToken.Type.THEN, identifier);
            case "else":
                return new MinionStrategyToken(MinionStrategyToken.Type.ELSE, identifier);
            case "while":
                return new MinionStrategyToken(MinionStrategyToken.Type.WHILE, identifier);
            case "done":
                return new MinionStrategyToken(MinionStrategyToken.Type.DONE, identifier);
            case "move":
                return new MinionStrategyToken(MinionStrategyToken.Type.MOVE, identifier);
            case "shoot":
                return new MinionStrategyToken(MinionStrategyToken.Type.SHOOT, identifier);
            case "up":
                return new MinionStrategyToken(MinionStrategyToken.Type.UP, identifier);
            case "down":
                return new MinionStrategyToken(MinionStrategyToken.Type.DOWN, identifier);
            case "upleft":
                return new MinionStrategyToken(MinionStrategyToken.Type.UPLEFT, identifier);
            case "upright":
                return new MinionStrategyToken(MinionStrategyToken.Type.UPRIGHT, identifier);
            case "downleft":
                return new MinionStrategyToken(MinionStrategyToken.Type.DOWNLEFT, identifier);
            case "downright":
                return new MinionStrategyToken(MinionStrategyToken.Type.DOWNRIGHT, identifier);
            case "nearby":
                return new MinionStrategyToken(MinionStrategyToken.Type.NEARBY, identifier);
            case "ally":
                return new MinionStrategyToken(MinionStrategyToken.Type.ALLY, identifier);
            case "opponent":
                return new MinionStrategyToken(MinionStrategyToken.Type.OPPONENT, identifier);
            default:
                return new MinionStrategyToken(MinionStrategyToken.Type.IDENTIFIER, identifier);
        }
    }
}
