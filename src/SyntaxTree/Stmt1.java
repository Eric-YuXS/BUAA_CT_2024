package SyntaxTree;

import frontend.Token;

public class Stmt1 extends Stmt {  // Stmt → LVal '=' Exp ';'
    private final LVal lVal;
    private final Token assign;
    private final Exp exp;
    private final Token semicn;

    public Stmt1(LVal lVal, Token assign, Exp exp, Token semicn) {
        super();
        this.lVal = lVal;
        this.assign = assign;
        this.exp = exp;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lVal).append(assign).append(exp);
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<Stmt>\n").toString();
    }
}
