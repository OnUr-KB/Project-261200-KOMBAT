package minion;


import minion.MinionStrategyToken;
import minion.MinionStrategyAST;

import java.util.ArrayList;
import java.util.List;

public class MinionStrategyParser {

    private final List<MinionStrategyToken> tokens;
    private int position;

    public MinionStrategyParser(List<MinionStrategyToken> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    public MinionStrategyAST.Statement parse() {
        return parseStatement();
    }

    private MinionStrategyAST.Statement parseStatement() {
        MinionStrategyToken current = peek();
        if (current.type == MinionStrategyToken.Type.IF) {
            return parseIfStatement();
        } else if (current.type == MinionStrategyToken.Type.WHILE) {
            return parseWhileStatement();
        } else if (current.type == MinionStrategyToken.Type.LBRACE) {
            return parseBlockStatement();
        } else if (current.type == MinionStrategyToken.Type.IDENTIFIER) {
            if (peek(1).type == MinionStrategyToken.Type.ASSIGN) {
                return parseAssignmentStatement();
            } else {
                return parseActionCommand();
            }
        } else if (current.type == MinionStrategyToken.Type.DONE || current.type == MinionStrategyToken.Type.MOVE || current.type == MinionStrategyToken.Type.SHOOT) {
            return parseActionCommand();
        } else {
            throw new RuntimeException("Unexpected token: " + current);
        }
    }

    private MinionStrategyAST.IfStatement parseIfStatement() {
        consume(MinionStrategyToken.Type.IF);
        consume(MinionStrategyToken.Type.LPAREN);
        MinionStrategyAST.Expression condition = parseExpression();
        consume(MinionStrategyToken.Type.RPAREN);
        consume(MinionStrategyToken.Type.THEN);
        MinionStrategyAST.Statement thenStatement = parseStatement();
        consume(MinionStrategyToken.Type.ELSE);
        MinionStrategyAST.Statement elseStatement = parseStatement();
        return new MinionStrategyAST.IfStatement(condition, thenStatement, elseStatement);
    }

    private MinionStrategyAST.WhileStatement parseWhileStatement() {
        consume(MinionStrategyToken.Type.WHILE);
        consume(MinionStrategyToken.Type.LPAREN);
        MinionStrategyAST.Expression condition = parseExpression();
        consume(MinionStrategyToken.Type.RPAREN);
        MinionStrategyAST.Statement statement = parseStatement();
        return new MinionStrategyAST.WhileStatement(condition, statement);
    }

    private MinionStrategyAST.BlockStatement parseBlockStatement() {
        consume(MinionStrategyToken.Type.LBRACE);
        List<MinionStrategyAST.Statement> statements = new ArrayList<>();
        while (peek().type!= MinionStrategyToken.Type.RBRACE) {
            statements.add(parseStatement());
        }
        consume(MinionStrategyToken.Type.RBRACE);
        return new MinionStrategyAST.BlockStatement(statements);
    }

    private MinionStrategyAST.AssignmentStatement parseAssignmentStatement() {
        MinionStrategyToken identifier = consume(MinionStrategyToken.Type.IDENTIFIER);
        consume(MinionStrategyToken.Type.ASSIGN);
        MinionStrategyAST.Expression expression = parseExpression();
        return new MinionStrategyAST.AssignmentStatement(identifier.lexeme, expression);
    }

    private MinionStrategyAST.ActionCommand parseActionCommand() {
        MinionStrategyToken current = peek();
        if (current.type == MinionStrategyToken.Type.DONE) {
            consume(MinionStrategyToken.Type.DONE);
            return new MinionStrategyAST.DoneCommand();
        } else if (current.type == MinionStrategyToken.Type.MOVE) {
            consume(MinionStrategyToken.Type.MOVE);
            MinionStrategyToken direction = parseDirection();
            return new MinionStrategyAST.MoveCommand(direction.lexeme);
        } else if (current.type == MinionStrategyToken.Type.SHOOT) {
            consume(MinionStrategyToken.Type.SHOOT);
            MinionStrategyToken direction = parseDirection();
            MinionStrategyAST.Expression expenditure = parseExpression();
            return new MinionStrategyAST.ShootCommand(direction.lexeme, expenditure);
        } else {
            throw new RuntimeException("Unexpected token: " + current);
        }
    }

    private MinionStrategyToken parseDirection() {
        MinionStrategyToken current = peek();
        if (current.type == MinionStrategyToken.Type.UP || current.type == MinionStrategyToken.Type.DOWN || current.type == MinionStrategyToken.Type.UPLEFT ||
                current.type == MinionStrategyToken.Type.UPRIGHT || current.type == MinionStrategyToken.Type.DOWNLEFT || current.type == MinionStrategyToken.Type.DOWNRIGHT) {
            return consume(current.type);
        } else {
            throw new RuntimeException("Unexpected token: " + current);
        }
    }

    public  MinionStrategyAST.Expression parseExpression() {
        MinionStrategyAST.Expression expression = parseTerm();
        while (peek().type == MinionStrategyToken.Type.PLUS || peek().type == MinionStrategyToken.Type.MINUS) {
            MinionStrategyToken operator = consume(peek().type);
            MinionStrategyAST.Expression right = parseTerm();
            if (operator.type == MinionStrategyToken.Type.PLUS) {
                expression = new MinionStrategyAST.BinaryExpression(MinionStrategyAST.BinaryExpression.Operator.PLUS, expression, right);
            } else {
                expression = new MinionStrategyAST.BinaryExpression(MinionStrategyAST.BinaryExpression.Operator.MINUS, expression, right);
            }
        }
        return expression;
    }

    private MinionStrategyAST.Expression parseTerm() {
        MinionStrategyAST.Expression expression = parseFactor();
        while (peek().type == MinionStrategyToken.Type.MULTIPLY || peek().type == MinionStrategyToken.Type.DIVIDE || peek().type == MinionStrategyToken.Type.MODULO) {
            MinionStrategyToken operator = consume(peek().type);
            MinionStrategyAST.Expression right = parseFactor();
            if (operator.type == MinionStrategyToken.Type.MULTIPLY) {
                expression = new MinionStrategyAST.BinaryExpression(MinionStrategyAST.BinaryExpression.Operator.MULTIPLY, expression, right);
            } else if (operator.type == MinionStrategyToken.Type.DIVIDE) {
                expression = new MinionStrategyAST.BinaryExpression(MinionStrategyAST.BinaryExpression.Operator.DIVIDE, expression, right);
            } else {
                expression = new MinionStrategyAST.BinaryExpression(MinionStrategyAST.BinaryExpression.Operator.MODULO, expression, right);
            }
        }
        return expression;
    }

    private MinionStrategyAST.Expression parseFactor() {
        MinionStrategyAST.Expression expression = parsePower();
        while (peek().type == MinionStrategyToken.Type.POWER) {
            MinionStrategyToken operator = consume(peek().type);
            MinionStrategyAST.Expression right = parsePower();
            expression = new MinionStrategyAST.BinaryExpression(MinionStrategyAST.BinaryExpression.Operator.POWER, expression, right);
        }
        return expression;
    }

    private MinionStrategyAST.Expression parsePower() {
        MinionStrategyToken current = peek();
        if (current.type == MinionStrategyToken.Type.NUMBER) {
            consume(MinionStrategyToken.Type.NUMBER);
            return new MinionStrategyAST.NumberExpression(Long.parseLong(current.lexeme));
        } else if (current.type == MinionStrategyToken.Type.IDENTIFIER) {
            consume(MinionStrategyToken.Type.IDENTIFIER);
            return new MinionStrategyAST.IdentifierExpression(current.lexeme);
        } else if (current.type == MinionStrategyToken.Type.LPAREN) {
            consume(MinionStrategyToken.Type.LPAREN);
            MinionStrategyAST.Expression expression = parseExpression();
            consume(MinionStrategyToken.Type.RPAREN);
            return expression;
        } else if (current.type == MinionStrategyToken.Type.ALLY || current.type == MinionStrategyToken.Type.OPPONENT) {
            consume(current.type);
            return new MinionStrategyAST.InfoExpression(current.type);
        } else if (current.type == MinionStrategyToken.Type.NEARBY) {
            consume(MinionStrategyToken.Type.NEARBY);
            MinionStrategyToken direction = parseDirection();
            return new MinionStrategyAST.NearbyExpression(direction.lexeme);
        } else {
            throw new RuntimeException("Unexpected token: " + current);
        }
    }

    private MinionStrategyToken peek() {
        return tokens.get(position);
    }

    private MinionStrategyToken peek(int offset) {
        return tokens.get(position + offset);
    }

    private MinionStrategyToken consume(MinionStrategyToken.Type type) {
        MinionStrategyToken current = peek();
        if (current.type == type) {
            position++;
            return current;
        } else {
            throw new RuntimeException("Expected token of type " + type + ", but got " + current);
        }
    }
}
