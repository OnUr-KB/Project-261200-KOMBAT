package kombat.project1_1.player;

import java.util.List;

public class BlockNode implements Node {
    public final List<Node> statements;

    public BlockNode(List<Node> statements) {
        this.statements = statements;
    }
}