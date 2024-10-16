package SyntaxTree;

import frontend.Token;

public class PrimaryExp1 extends PrimaryExp {  // PrimaryExp → '(' Exp ')'
    private final Token lParent;
    private final Exp exp;
    private final Token rParent;

    public PrimaryExp1(Token lParent, Exp exp, Token rParent) {
        super();
        this.lParent = lParent;
        this.exp = exp;
        this.rParent = rParent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lParent).append(exp);
        if (rParent != null) {
            sb.append(rParent);
        }
        return sb.append("<PrimaryExp>\n").toString();
    }
}
