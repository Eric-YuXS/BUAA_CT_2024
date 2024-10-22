package SyntaxTree;

import frontend.Error;
import frontend.FuncSymbol;
import frontend.SymbolStack;
import frontend.Token;

public class Stmt6 extends Stmt {  // Stmt → 'break' ';'
    private final Token breakTk;
    private final Token semicn;

    public Stmt6(Token breakTk, Token semicn) {
        this.breakTk = breakTk;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(breakTk);
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<Stmt>\n").toString();
    }

    @Override
    public void analyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean isLoop) {
        if (!isLoop) {
            symbolStack.addError(new Error(breakTk.getLineNumber(), 'm'));
        }
    }
}
