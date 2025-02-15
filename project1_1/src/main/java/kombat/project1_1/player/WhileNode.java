package kombat.project1_1.player;

public class WhileNode implements Node {
    public final Node condition;
    public final Node statement;

    public WhileNode(Node condition, Node statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public void execute() {

    }
}