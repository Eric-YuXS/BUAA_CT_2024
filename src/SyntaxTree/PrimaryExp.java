package SyntaxTree;

import frontend.SymbolStack;
import frontend.SymbolType;

public class PrimaryExp implements SyntaxTreeNode {  // PrimaryExp → '(' Exp ')' | LVal | Number | Character
    @Override
    public String toString() {
        return "super<PrimaryExp>\n";
    }

    public SymbolType analyze(SymbolStack symbolStack) {
        System.err.println("super<PrimaryExp>");
        return null;
    }
}
