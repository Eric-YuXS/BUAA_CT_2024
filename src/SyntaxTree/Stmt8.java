package SyntaxTree;

import frontend.Error;
import frontend.FuncSymbol;
import frontend.SymbolStack;
import frontend.SymbolType;
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

    @Override
    public void analyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean isLoop) {
        if (funcSymbol.getSymbolType() == SymbolType.VoidFunc && exp != null) {
            symbolStack.addError(new Error(returnTk.getLineNumber(), 'f'));
//        } else if (funcSymbol.getSymbolType() != SymbolType.VoidFunc && exp == null) {
//            System.err.println("Return without exp in " + funcSymbol);
        }
    }
}
