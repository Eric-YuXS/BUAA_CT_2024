package SyntaxTree;

import frontend.FuncSymbol;
import frontend.SymbolStack;
import frontend.Token;

public class Stmt4 extends Stmt {  // Stmt → 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
    private final Token ifTk;
    private final Token lParent;
    private final Cond cond;
    private final Token rParent;
    private final Stmt stmt;
    private final Token elseTk;
    private final Stmt elseStmt;

    public Stmt4(Token ifTk, Token lParent, Cond cond, Token rParent, Stmt stmt, Token elseTk, Stmt elseStmt) {
        super();
        this.ifTk = ifTk;
        this.lParent = lParent;
        this.cond = cond;
        this.rParent = rParent;
        this.stmt = stmt;
        this.elseTk = elseTk;
        this.elseStmt = elseStmt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ifTk).append(lParent).append(cond);
        if (rParent != null) {
            sb.append(rParent);
        }
        sb.append(stmt);
        if (elseTk != null) {
            sb.append(elseTk).append(elseStmt);
        }
        return sb.append("<Stmt>\n").toString();
    }

    @Override
    public void analyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean isLoop) {
        stmt.analyze(symbolStack, funcSymbol, isLoop);
        if (elseStmt != null) {
            elseStmt.analyze(symbolStack, funcSymbol, isLoop);
        }
    }
}
