package SyntaxTree;

import frontend.Token;

public class VarDef implements SyntaxTreeNode {  // VarDef → Ident [ '[' ConstExp ']' ] | Ident [ '[' ConstExp ']' ] '=' InitVal
    private final Token ident;
    private final Token lBrack;
    private final ConstExp constExp;
    private final Token rBrack;
    private final Token assign;
    private final InitVal initVal;

    public VarDef(Token ident, Token lBrack, ConstExp constExp, Token rBrack, Token assign, InitVal initVal) {
        this.ident = ident;
        this.lBrack = lBrack;
        this.constExp = constExp;
        this.rBrack = rBrack;
        this.assign = assign;
        this.initVal = initVal;
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
        if (assign != null) {
            sb.append(assign).append(initVal);
        }
        return sb.append("<VarDef>\n").toString();
    }
}
