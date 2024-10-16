package SyntaxTree;

import frontend.Token;

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
}
