package SyntaxTree;

public class Stmt3 extends Stmt {  // Stmt → Block
    private final Block block;

    public Stmt3(Block block) {
        super();
        this.block = block;
    }

    @Override
    public String toString() {
        return block + "<Stmt>\n";
    }
}
