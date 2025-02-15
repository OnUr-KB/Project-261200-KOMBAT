package kombat.project1_1.player;
import java.util.List;

public class StatementListNode implements Node {
    private List<Node> statements;

    // constructor
    public StatementListNode(List<Node> statements) {
        this.statements = statements;
    }

    // Getter method for statements
    public List<Node> getStatements() {
        return statements;
    }

    // implement เมธอด execute
    @Override
    public void execute() {
        // การทำงานของ execute (ตามที่ต้องการ)
        for (Node statement : statements) {
            statement.execute();
            System.out.println("Executing statement: " + statement);
        }
    }
}
