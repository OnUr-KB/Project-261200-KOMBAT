package kombat.project1_1.player;

public class BinaryOperatorNode implements Node {
    public final TokenType operator;
    public final Node left;
    public final Node right;

    public BinaryOperatorNode(TokenType operator, Node left, Node right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }
}