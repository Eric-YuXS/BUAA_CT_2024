package SyntaxTree;

import frontend.SymbolStack;
import frontend.SymbolType;

public class UnaryExp implements SyntaxTreeNode {  // UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    @Override
    public String toString() {
        return "super<UnaryExp>\n";
    }

    public SymbolType analyze(SymbolStack symbolStack) {
        System.err.println("super<UnaryExp>");
        return null;
    }
}
