package kombat.project1_1.player;

public class ShootNode implements Node {
    public final String direction;
    public final Node expression;

    public ShootNode(String direction, Node expression) {
        this.direction = direction;
        this.expression = expression;
    }

    @Override
    public void execute() {

    }
}