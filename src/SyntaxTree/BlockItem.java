package SyntaxTree;

import frontend.FuncSymbol;
import frontend.SymbolStack;

public class BlockItem implements SyntaxTreeNode {  // BlockItem → Decl | Stmt
    @Override
    public String toString() {
        return "super<BlockItem>\n";
    }

    public void analyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean isLoop) {
        System.err.println("super<BlockItem>");
    }

    public boolean analyzeReturn(SymbolStack symbolStack) {
        System.err.println("super<BlockItem>");
        return false;
    }
}
