package kombat.project1_1.player;

public enum TokenType {
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    THEN("then"),
    DONE("done"),
    MOVE("move"),
    SHOOT("shoot"),
    UP("up"),
    DOWN("down"),
    UPLEFT("upleft"),
    UPRIGHT("upright"),
    DOWNLEFT("downleft"),
    DOWNRIGHT("downright"),
    NEARBY("nearby"),
    ALLY("ally"),
    OPPONENT("opponent"),
    LBRACE("{"),
    RBRACE("}"),
    LPAREN("("),
    RPAREN(")"),
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULO("%"),
    POWER("^"),
    ASSIGN("="),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    NUMBER(null),
    IDENTIFIER(null);

    private final String keyword;

    TokenType(String keyword) {
        this.keyword = keyword;
    }

    public static TokenType fromString(String value) {
        for (TokenType type: values()) {
            if (type.keyword!= null && type.keyword.equals(value)) {
                return type;
            }
        }
        if (value.matches("\\d+")) {
            return NUMBER;
        } else if (value.matches("\\w+")) {
            return IDENTIFIER;
        } else {
            throw new IllegalArgumentException("Invalid token value: " + value);
        }
    }

    public boolean isDirection() {
        return this == UP || this == DOWN || this == UPLEFT || this == UPRIGHT || this == DOWNLEFT
                || this == DOWNRIGHT;
    }
}