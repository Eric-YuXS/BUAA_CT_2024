package SyntaxTree;

import frontend.SymbolStack;

public class InitVal1 extends InitVal {  // InitVal → Exp
    private final Exp exp;

    public InitVal1(Exp exp) {
        super();
        this.exp = exp;
    }

    @Override
    public String toString() {
        return exp + "<InitVal>\n";
    }

    @Override
    public void analyze(SymbolStack symbolStack) {
        exp.analyze(symbolStack);
    }
}
