package SyntaxTree;

import frontend.SymbolStack;

public class ConstInitVal1 extends ConstInitVal {  // ConstInitVal → ConstExp
    private final ConstExp constExp;

    public ConstInitVal1(ConstExp constExp) {
        super();
        this.constExp = constExp;
    }

    @Override
    public String toString() {
        return constExp + "<ConstInitVal>\n";
    }

    @Override
    public void analyze(SymbolStack symbolStack) {
        constExp.analyze(symbolStack);
    }
}
