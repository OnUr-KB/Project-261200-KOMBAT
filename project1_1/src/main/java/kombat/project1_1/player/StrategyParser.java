package kombat.project1_1.player;

import java.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrategyParser {

    public static Node parse(String code) {
        List<Token> tokens = tokenize(code);
        return parseStatementList(tokens);
    }

    private static List<Token> tokenize(String code) {
        List<Token> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile(
                "\\b(if|else|while|then|done|move|shoot|up|down|upleft|upright|downleft|downright|nearby|ally|opponent|\\{|\\}|\\(|\\)|\\+|-|\\*|/|%|\\^|=|<|>|\\d+|\\w+)\\b");
        Matcher matcher = pattern.matcher(code);
        while (matcher.find()) {
            String value = matcher.group();
            TokenType type = TokenType.fromString(value);
            tokens.add(new Token(type, value));
        }
        return tokens;
    }

    private static Node parseStatementList(List<Token> tokens) {
        List<Node> statements = new ArrayList<>();
        while (!tokens.isEmpty()) {
            statements.add(parseStatement(tokens));
        }
        return new StatementListNode(statements);
    }

    private static Node parseStatement(List<Token> tokens) {
        Token token = tokens.remove(0);
        switch (token.type) {
            case IF:
                return parseIfStatement(tokens);
            case WHILE:
                return parseWhileStatement(tokens);
            case IDENTIFIER:
                if (tokens.get(0).type == TokenType.ASSIGN) {
                    return parseAssignmentStatement(token, tokens);
                } else {
                    return parseActionCommand(token, tokens);
                }
            case LBRACE:
                return parseBlockStatement(tokens);
            default:
                throw new IllegalArgumentException("Invalid statement starting with: " + token);
        }
    }

    private static Node parseIfStatement(List<Token> tokens) {
        Node condition = parseExpression(tokens);
        consumeToken(tokens, TokenType.THEN);
        Node thenStatement = parseStatement(tokens);
        consumeToken(tokens, TokenType.ELSE);
        Node elseStatement = parseStatement(tokens);
        return new IfNode(condition, thenStatement, elseStatement);
    }

    private static Node parseWhileStatement(List<Token> tokens) {
        Node condition = parseExpression(tokens);
        Node statement = parseStatement(tokens);
        return new WhileNode(condition, statement);
    }

    private static Node parseAssignmentStatement(Token identifier, List<Token> tokens) {
        consumeToken(tokens, TokenType.ASSIGN);
        Node expression = parseExpression(tokens);
        return new AssignmentNode(identifier.value, expression);
    }

    private static Node parseActionCommand(Token identifier, List<Token> tokens) {
        switch (identifier.value) {
            case "done":
                return new DoneNode();
            case "move":
                return parseMoveCommand(tokens);
            case "shoot":
                return parseShootCommand(tokens);
            default:
                throw new IllegalArgumentException("Invalid action command: " + identifier);
        }
    }

    private static Node parseMoveCommand(List<Token> tokens) {
        Token direction = tokens.remove(0);
        if (!direction.type.isDirection()) {
            throw new IllegalArgumentException("Invalid direction for move command: " + direction);
        }
        return new MoveNode(direction.value);
    }

    private static Node parseShootCommand(List<Token> tokens) {
        Token direction = tokens.remove(0);
        if (!direction.type.isDirection()) {
            throw new IllegalArgumentException("Invalid direction for shoot command: " + direction);
        }
        Node expression = parseExpression(tokens);
        return new ShootNode(direction.value, expression);
    }

    private static Node parseBlockStatement(List<Token> tokens) {
        List<Node> statements = new ArrayList<>();
        while (tokens.get(0).type!= TokenType.RBRACE) {
            statements.add(parseStatement(tokens));
        }
        consumeToken(tokens, TokenType.RBRACE);
        return new BlockNode(statements);
    }

    private static Node parseExpression(List<Token> tokens) {
        Node left = parseTerm(tokens);
        while (!tokens.isEmpty() && (tokens.get(0).type == TokenType.PLUS
                || tokens.get(0).type == TokenType.MINUS)) {
            Token operator = tokens.remove(0);
            Node right = parseTerm(tokens);
            left = new BinaryOperatorNode(operator.type, left, right);
        }
        return left;
    }

    private static Node parseTerm(List<Token> tokens) {
        Node left = parseFactor(tokens);
        while (!tokens.isEmpty() && (tokens.get(0).type == TokenType.MULTIPLY
                || tokens.get(0).type == TokenType.DIVIDE
                || tokens.get(0).type == TokenType.MODULO)) {
            Token operator = tokens.remove(0);
            Node right = parseFactor(tokens);
            left = new BinaryOperatorNode(operator.type, left, right);
        }
        return left;
    }

    private static Node parseFactor(List<Token> tokens) {
        Node left = parsePower(tokens);
        while (!tokens.isEmpty() && tokens.get(0).type == TokenType.POWER) {
            Token operator = tokens.remove(0);
            Node right = parsePower(tokens);
            left = new BinaryOperatorNode(operator.type, left, right);
        }
        return left;
    }

    private static Node parsePower(List<Token> tokens) {
        Token token = tokens.remove(0);
        switch (token.type) {
            case NUMBER:
                return new NumberNode(Long.parseLong(token.value));
            case IDENTIFIER:
                return new IdentifierNode(token.value);
            case LPAREN:
                Node expression = parseExpression(tokens);
                consumeToken(tokens, TokenType.RPAREN);
                return expression;
            case NEARBY:
            case ALLY:
            case OPPONENT:
                return parseInfoExpression(token, tokens);
            default:
                throw new IllegalArgumentException("Invalid power: " + token);
        }
    }

    private static Node parseInfoExpression(Token token, List<Token> tokens) {
        switch (token.type) {
            case NEARBY:
                Token direction = tokens.remove(0);
                if (!direction.type.isDirection()) {
                    throw new IllegalArgumentException("Invalid direction for nearby function: " + direction);
                }
                return new NearbyNode(direction.value);
            case ALLY:
                return new AllyNode();
            case OPPONENT:
                return new OpponentNode();
            default:
                throw new IllegalArgumentException("Invalid info expression: " + token);
        }
    }

    private static void consumeToken(List<Token> tokens, TokenType expectedType) {
        Token token = tokens.remove(0);
        if (token.type!= expectedType) {
            throw new IllegalArgumentException("Expected token type: " + expectedType + ", but found: " + token);
        }
    }
}