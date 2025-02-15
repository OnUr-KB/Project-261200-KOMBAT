package project.kombat1.minion;

import java.util.List;

public class MinionStrategyAST {

    public interface Statement {
    }

    public static class IfStatement implements Statement {
        public final Expression condition;
        public final Statement thenStatement;
        public final Statement elseStatement;

        public IfStatement(Expression condition, Statement thenStatement, Statement elseStatement) {
            this.condition = condition;
            this.thenStatement = thenStatement;
            this.elseStatement = elseStatement;
        }
    }

    public static class WhileStatement implements Statement {
        public final Expression condition;
        public final Statement statement;

        public WhileStatement(Expression condition, Statement statement) {
            this.condition = condition;
            this.statement = statement;
        }
    }

    public static class BlockStatement implements Statement {
        public final List<Statement> statements;

        public BlockStatement(List<Statement> statements) {
            this.statements = statements;
        }
    }

    public static class AssignmentStatement implements Statement {
        public final String identifier;
        public final Expression expression;

        public AssignmentStatement(String identifier, Expression expression) {
            this.identifier = identifier;
            this.expression = expression;
        }
    }

    public interface ActionCommand extends Statement {
    }

    public static class DoneCommand implements ActionCommand {
    }

    public static class MoveCommand implements ActionCommand {
        public final String direction;

        public MoveCommand(String direction) {
            this.direction = direction;
        }
    }

    public static class ShootCommand implements ActionCommand {
        public final String direction;
        public final Expression expenditure;

        public ShootCommand(String direction, Expression expenditure) {
            this.direction = direction;
            this.expenditure = expenditure;
        }
    }

    public interface Expression {
    }

    public static class BinaryExpression implements Expression {
        public enum Operator {
            PLUS,
            MINUS,
            MULTIPLY,
            DIVIDE,
            MODULO,
            POWER,
            GREATER_THAN
        }

        public final Operator operator;
        public final Expression left;
        public final Expression right;

        public BinaryExpression(Operator operator, Expression left, Expression right) {
            this.operator = operator;
            this.left = left;
            this.right = right;
        }
    }

    public static class NumberExpression implements Expression {
        public final long value;

        public NumberExpression(long value) {
            this.value = value;
        }
    }

    public static class IdentifierExpression implements Expression {
        public final String identifier;

        public IdentifierExpression(String identifier) {
            this.identifier = identifier;
        }
    }

    public static class InfoExpression implements Expression {
        public final MinionStrategyToken.Type type;

        public InfoExpression(MinionStrategyToken.Type type) {
            this.type = type;
        }
    }

    public static class NearbyExpression implements Expression {
        public final String direction;

        public NearbyExpression(String direction) {
            this.direction = direction;
        }
    }
}