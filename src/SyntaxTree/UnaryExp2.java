package SyntaxTree;

import frontend.*;
import frontend.Error;

public class UnaryExp2 extends UnaryExp {  // UnaryExp → Ident '(' [FuncRParams] ')'
    private final Token ident;
    private final Token lParent;
    private final FuncRParams funcRParams;
    private final Token rParent;

    public UnaryExp2(Token ident, Token lParent, FuncRParams funcRParams, Token rParent) {
        super();
        this.ident = ident;
        this.lParent = lParent;
        this.funcRParams = funcRParams;
        this.rParent = rParent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident).append(lParent);
        if (funcRParams != null) {
            sb.append(funcRParams);
        }
        if (rParent != null) {
            sb.append(rParent);
        }
        return sb.append("<UnaryExp>\n").toString();
    }

    public SymbolType analyze(SymbolStack symbolStack) {
        Symbol symbol = symbolStack.getSymbol(ident.getString());
        if (symbol == null) {
            symbolStack.addError(new Error(ident.getLineNumber(), 'c'));
            funcRParams.analyze(symbolStack);
            return null;
        } else if (symbol instanceof FuncSymbol) {
            funcRParams.analyze(symbolStack, (FuncSymbol) symbol, ident);
            return symbol.getSymbolType().funcToVarOrVoid();
        } else {
            System.err.println(ident.getString() + "is not a function!");
            return null;
        }
    }
}
