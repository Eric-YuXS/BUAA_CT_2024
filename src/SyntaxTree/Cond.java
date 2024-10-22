package SyntaxTree;

import frontend.SymbolStack;

public class Cond implements SyntaxTreeNode {  // Cond → LOrExp
    private final LOrExp lOrExp;

    public Cond(LOrExp lOrExp) {
        this.lOrExp = lOrExp;
    }

    @Override
    public String toString() {
        return lOrExp + "<Cond>\n";
    }

    public void analyze(SymbolStack symbolStack) {
        lOrExp.analyze(symbolStack);
    }
}
