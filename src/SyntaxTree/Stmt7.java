package SyntaxTree;

import frontend.SymbolStack;
import frontend.Token;

public class Stmt7 extends Stmt {  // Stmt → 'continue' ';'
    private final Token continueTk;
    private final Token semicn;

    public Stmt7(Token continueTk, Token semicn) {
        this.continueTk = continueTk;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(continueTk);
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<Stmt>\n").toString();
    }

    public void analyze(SymbolStack symbolStack) {
    }
}
