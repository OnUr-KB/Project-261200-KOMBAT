package kombat.project1_1.player;

public class IfNode implements Node {
    public final Node condition;
    public final Node thenStatement;
    public final Node elseStatement;

    public IfNode(Node condition, Node thenStatement, Node elseStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public void execute() {

    }
}