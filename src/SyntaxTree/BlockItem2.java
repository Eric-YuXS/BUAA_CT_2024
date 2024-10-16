package SyntaxTree;

public class BlockItem2 extends BlockItem {  // BlockItem → Stmt
    private final Stmt stmt;

    public BlockItem2(Stmt stmt) {
        super();
        this.stmt = stmt;
    }

    @Override
    public String toString() {
        return stmt.toString();
    }
}
