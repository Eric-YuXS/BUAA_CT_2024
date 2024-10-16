package SyntaxTree;

import frontend.Token;

public class Stmt10 extends Stmt {  // Stmt → LVal '=' 'getchar''('')'';'
    private final LVal lVal;
    private final Token assign;
    private final Token getchar;
    private final Token lParent;
    private final Token rParent;
    private final Token semicn;

    public Stmt10(LVal lVal, Token assign, Token getchar, Token lParent, Token rParent, Token semicn) {
        super();
        this.lVal = lVal;
        this.assign = assign;
        this.getchar = getchar;
        this.lParent = lParent;
        this.rParent = rParent;
        this.semicn = semicn;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(lVal).append(assign).append(getchar).append(lParent);
        if (rParent != null) {
            sb.append(rParent);
        }
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<Stmt>\n").toString();
    }
}
