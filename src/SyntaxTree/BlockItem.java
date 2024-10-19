package SyntaxTree;

import frontend.SymbolStack;

public class BlockItem implements SyntaxTreeNode {  // BlockItem → Decl | Stmt
    @Override
    public String toString() {
        return "super<BlockItem>\n";
    }

    public void analyze(SymbolStack symbolStack) {
        System.err.println("super<BlockItem>");
    }
}
