package SyntaxTree;

import frontend.Token;

public class Stmt2 extends Stmt {  // Stmt → [Exp] ';'
    private final Exp exp;
    private final Token semicn;

    public Stmt2(Exp exp, Token semicn) {
        super();
        this.exp = exp;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (exp != null) {
            sb.append(exp);
        }
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<Stmt>\n").toString();
    }
}
