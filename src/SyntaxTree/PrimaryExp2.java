package SyntaxTree;

import frontend.SymbolStack;
import frontend.SymbolType;

public class PrimaryExp2 extends PrimaryExp {  // PrimaryExp → LVal
    private final LVal lVal;

    public PrimaryExp2(LVal lVal) {
        super();
        this.lVal = lVal;
    }

    @Override
    public String toString() {
        return lVal + "<PrimaryExp>\n";
    }

    public SymbolType analyze(SymbolStack symbolStack) {
        return lVal.analyze(symbolStack);
    }
}
