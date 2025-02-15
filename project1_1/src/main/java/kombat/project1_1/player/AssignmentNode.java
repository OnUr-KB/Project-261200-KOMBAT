package kombat.project1_1.player;

public class AssignmentNode implements Node {
    public final String identifier;
    public final Node expression;

    public AssignmentNode(String identifier, Node expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public void execute() {

    }
}