package SyntaxTree;

import frontend.SymbolStack;
import frontend.SymbolType;

public class UnaryExp3 extends UnaryExp {  // UnaryExp → UnaryOp UnaryExp
    private final UnaryOp unaryOp;
    private final UnaryExp unaryExp;

    public UnaryExp3(UnaryOp unaryOp, UnaryExp unaryExp) {
        super();
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    @Override
    public String toString() {
        return unaryOp.toString() + unaryExp + "<UnaryExp>\n";
    }

    @Override
    public SymbolType analyze(SymbolStack symbolStack) {
        SymbolType symbolType = unaryExp.analyze(symbolStack);
        if (symbolType != SymbolType.VoidFunc) {
            return symbolType;
        } else {
            System.err.println("Calculate with void!");
            return null;
        }
    }
}
