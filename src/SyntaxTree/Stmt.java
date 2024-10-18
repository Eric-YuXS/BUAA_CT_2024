package SyntaxTree;

import frontend.SymbolStack;

public class Stmt implements SyntaxTreeNode {
    /* Stmt → LVal '=' Exp ';'
            | [Exp] ';'
            | Block
			| 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
            | 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
			| 'break' ';'
			| 'continue' ';'
            | 'return' [Exp] ';'
            | LVal '=' 'getint''('')'';'
            | LVal '=' 'getchar''('')'';'
            | 'printf''('StringConst {','Exp}')'';'
    */
    @Override
    public String toString() {
        return "super<Stmt>\n";
    }

    public void analyze(SymbolStack symbolStack) {
        System.err.println("super<Stmt>");
    }
}
