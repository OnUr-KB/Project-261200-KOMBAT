package main.minion;


import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MinionStrategyToken token = (MinionStrategyToken) obj;
        return type == token.type && Objects.equals(lexeme, token.lexeme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, lexeme);
    }

}