package SyntaxTree;

import frontend.FuncSymbol;
import frontend.SymbolStack;
import frontend.Token;

public class Stmt9 extends Stmt {  // Stmt → LVal '=' 'getint''('')'';'
    private final LVal lVal;
    private final Token assign;
    private final Token getint;
    private final Token lParent;
    private final Token rParent;
    private final Token semicn;

    public Stmt9(LVal lVal, Token assign, Token getint, Token lParent, Token rParent, Token semicn) {
        super();
        this.lVal = lVal;
        this.assign = assign;
        this.getint = getint;
        this.lParent = lParent;
        this.rParent = rParent;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lVal).append(assign).append(getint).append(lParent);
        if (rParent != null) {
            sb.append(rParent);
        }
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<Stmt>\n").toString();
    }

    @Override
    public void analyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean isLoop) {
        lVal.analyze(symbolStack, true);
    }
}
