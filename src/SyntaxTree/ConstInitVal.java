package SyntaxTree;

import frontend.SymbolStack;

public class ConstInitVal implements SyntaxTreeNode {  // ConstInitVal → ConstExp | '{' [ ConstExp { ',' ConstExp } ] '}' | StringConst
    @Override
    public String toString() {
        return "super<ConstInitVal>\n";
    }

    public void analyze(SymbolStack symbolStack) {
        System.out.println("super<ConstInitVal>");
    }
}
