package SyntaxTree;

import frontend.SymbolStack;

public class Decl implements SyntaxTreeNode {  // Decl → ConstDecl | VarDecl
    @Override
    public String toString() {
        return "super<Decl>\n";
    }

    public void analyze(SymbolStack symbolStack) {
        System.err.println("super<Decl>");
    }
}
