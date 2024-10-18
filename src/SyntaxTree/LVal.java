package SyntaxTree;

import frontend.*;
import frontend.Error;

public class LVal implements SyntaxTreeNode {  // LVal → Ident ['[' Exp ']']
    private final Token ident;
    private final Token lBrack;
    private final Exp exp;
    private final Token rBrack;

    public LVal(Token ident, Token lBrack, Exp exp, Token rBrack) {
        this.ident = ident;
        this.lBrack = lBrack;
        this.exp = exp;
        this.rBrack = rBrack;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident);
        if (lBrack != null) {
            sb.append(lBrack).append(exp);
            if (rBrack != null) {
                sb.append(rBrack);
            }
        }
        return sb.append("<LVal>\n").toString();
    }

    public SymbolType analyze(SymbolStack symbolStack) {
        Symbol symbol = symbolStack.getSymbol(ident.getString());
        if (symbol == null) {
            symbolStack.addError(new Error(ident.getLineNumber(), 'c'));
//        } else if (exp != null && (symbol.getSymbolType().isVar() || symbol.getSymbolType().isFunc())) {
//            System.err.println(ident.getString() + "is not an array!");
//            return null;
//        } else if (exp == null && (symbol.getSymbolType().isArray() || symbol.getSymbolType().isFunc())) {
//            System.err.println(ident.getString() + "is not a variable!");
//            return null;
        } else if (symbol.getSymbolType().isConst()) {
            symbolStack.addError(new Error(ident.getLineNumber(), 'h'));
        }
        if (exp != null) {
            exp.analyze(symbolStack);
            return symbol != null ? symbol.getSymbolType().arrayToVar() : null;
        } else {
            return symbol != null ? symbol.getSymbolType() : null;
        }
    }
}
