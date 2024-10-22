package SyntaxTree;

import frontend.FuncSymbol;
import frontend.SymbolStack;
import frontend.Token;

public class Stmt5 extends Stmt {  // Stmt → 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
    private final Token forTk;
    private final Token lParent;
    private final ForStmt forStmt1;
    private final Token semicn1;
    private final Cond cond;
    private final Token semicn2;
    private final ForStmt forStmt2;
    private final Token rParent;
    private final Stmt stmt;

    public Stmt5(Token forTk, Token lParent, ForStmt forStmt1, Token semicn1, Cond cond, Token semicn2,
                 ForStmt forStmt2, Token rParent, Stmt stmt) {
        super();
        this.forTk = forTk;
        this.lParent = lParent;
        this.forStmt1 = forStmt1;
        this.semicn1 = semicn1;
        this.cond = cond;
        this.semicn2 = semicn2;
        this.forStmt2 = forStmt2;
        this.rParent = rParent;
        this.stmt = stmt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(forTk).append(lParent);
        if (forStmt1 != null) {
            sb.append(forStmt1);
        }
        sb.append(semicn1);
        if (cond != null) {
            sb.append(cond);
        }
        sb.append(semicn2);
        if (forStmt2 != null) {
            sb.append(forStmt2);
        }
        if (rParent != null) {
            sb.append(rParent);
        }
        return sb.append(stmt).append("<Stmt>\n").toString();
    }

    @Override
    public void analyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean isLoop) {
        if (forStmt1 != null) {
            forStmt1.analyze(symbolStack);
        }
        if (cond != null) {
            cond.analyze(symbolStack);
        }
        if (forStmt2 != null) {
            forStmt2.analyze(symbolStack);
        }
        stmt.analyze(symbolStack, funcSymbol, true);
    }
}
