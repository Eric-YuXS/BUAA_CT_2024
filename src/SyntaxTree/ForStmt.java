package SyntaxTree;

import frontend.Token;

public class ForStmt implements SyntaxTreeNode {  // ForStmt → LVal '=' Exp
    private final LVal lVal;
    private final Token assign;
    private final Exp exp;

    public ForStmt(LVal lVal, Token assign, Exp exp) {
        this.lVal = lVal;
        this.assign = assign;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return lVal.toString() + assign + exp + "<ForStmt>\n";
    }
}
