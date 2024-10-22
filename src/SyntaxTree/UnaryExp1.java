package SyntaxTree;

import frontend.SymbolStack;
import frontend.SymbolType;

public class UnaryExp1 extends UnaryExp {  // UnaryExp → PrimaryExp
    private final PrimaryExp primaryExp;

    public UnaryExp1(PrimaryExp primaryExp) {
        super();
        this.primaryExp = primaryExp;
    }

    @Override
    public String toString() {
        return primaryExp + "<UnaryExp>\n";
    }

    @Override
    public SymbolType analyze(SymbolStack symbolStack) {
        return primaryExp.analyze(symbolStack);
    }
}
