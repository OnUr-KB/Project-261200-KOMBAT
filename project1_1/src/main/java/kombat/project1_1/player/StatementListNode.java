package kombat.project1_1.player;
import java.util.List;

public class StatementListNode implements Node {
    private List<String> statements;

    // constructor
    public StatementListNode(List<Node> statements) {
        this.statements = statements;
    }

    // implement เมธอด execute
    @Override
    public void execute() {
        // การทำงานของ execute (ตามที่ต้องการ)
        for (String statement : statements) {
            System.out.println("Executing statement: " + statement);
        }
    }
}
