package SyntaxTree;

import frontend.Token;

public class Stmt8 extends Stmt {  // Stmt → 'return' [Exp] ';'
    private final Token returnTk;
    private final Exp exp;
    private final Token semicn;

    public Stmt8(Token returnTk, Exp exp, Token semicn) {
        super();
        this.returnTk = returnTk;
        this.exp = exp;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(returnTk);
        if (exp != null) {
            sb.append(exp);
        }
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<Stmt>\n").toString();
    }
}
