package SyntaxTree;

import frontend.Error;
import frontend.Symbol;
import frontend.SymbolStack;
import frontend.SymbolType;
import frontend.Token;

public class ConstDef implements SyntaxTreeNode {  // ConstDef → Ident [ '[' ConstExp ']' ] '=' ConstInitVal
    private final Token ident;
    private final Token lBrack;
    private final ConstExp constExp;
    private final Token rBrack;
    private final Token assign;
    private final ConstInitVal constInitVal;

    public ConstDef(Token ident, Token lBrack, ConstExp constExp, Token rBrack, Token assign, ConstInitVal constInitVal) {
        this.ident = ident;
        this.lBrack = lBrack;
        this.constExp = constExp;
        this.rBrack = rBrack;
        this.assign = assign;
        this.constInitVal = constInitVal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident);
        if (lBrack != null) {
            sb.append(lBrack).append(constExp);
            if (rBrack != null) {
                sb.append(rBrack);
            }
        }
        return sb.append(assign).append(constInitVal).append("<ConstDef>\n").toString();
    }

    public void analyze(SymbolStack symbolStack, SymbolType symbolType) {
        if (constExp != null) {
            symbolType = symbolType.constToConstArray();
            constExp.analyze(symbolStack);
        }
        if (!symbolStack.addSymbol(new Symbol(symbolType, ident.getString()))) {
            symbolStack.addError(new Error(ident.getLineNumber(), 'b'));
        }
        constInitVal.analyze(symbolStack);
    }
}
