package SyntaxTree;

public class BlockItem1 extends BlockItem {  // BlockItem → Decl
    private final Decl decl;

    public BlockItem1(Decl decl) {
        super();
        this.decl = decl;
    }

    @Override
    public String toString() {
        return decl.toString();
    }
}
