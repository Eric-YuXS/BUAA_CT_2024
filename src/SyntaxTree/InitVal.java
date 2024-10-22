package SyntaxTree;

import frontend.SymbolStack;

public class InitVal implements SyntaxTreeNode {  // InitVal → Exp | '{' [ Exp { ',' Exp } ] '}' | StringConst
    @Override
    public String toString() {
        return "super<InitVal>\n";
    }

    public void analyze(SymbolStack symbolStack) {
        System.err.println("super<InitVal>");
    }
}
