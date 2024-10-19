package SyntaxTree;

import frontend.SymbolStack;
import frontend.SymbolType;

public class Exp implements SyntaxTreeNode {  // Exp → AddExp
    private final AddExp addExp;

    public Exp(AddExp addExp) {
        this.addExp = addExp;
    }

    @Override
    public String toString() {
        return addExp + "<Exp>\n";
    }

    public SymbolType analyze(SymbolStack symbolStack) {
        return addExp.analyze(symbolStack);
    }
}
