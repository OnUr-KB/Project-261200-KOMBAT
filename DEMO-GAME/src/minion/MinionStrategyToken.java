package minion;



public class MinionStrategyToken {

    public enum Type {
        NUMBER,
        IDENTIFIER,
        ASSIGN,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        MODULO,
        LPAREN,
        RPAREN,
        LBRACE,
        RBRACE,
        IF,
        THEN,
        ELSE,
        WHILE,
        DONE,
        MOVE,
        SHOOT,
        UP,
        DOWN,
        UPLEFT,
        UPRIGHT,
        DOWNLEFT,
        DOWNRIGHT,
        NEARBY,
        ALLY,
        OPPONENT,
        DOLLAR,
        POWER
    }

    public final Type type;
    public final String lexeme;

    public MinionStrategyToken(Type type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", lexeme='" + lexeme + '\'' +
                '}';
    }
}