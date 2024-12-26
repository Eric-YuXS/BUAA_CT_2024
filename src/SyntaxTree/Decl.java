package SyntaxTree;

import LLVMIR.Module;
import frontend.SymbolStack;

public class Decl implements SyntaxTreeNode {  // Decl → ConstDecl | VarDecl
    @Override
    public String toString() {
        return "super<Decl>\n";
    }

    public void errorAnalyze(SymbolStack symbolStack) {
        System.err.println("super<Decl>");
    }

    public void analyze(SymbolStack symbolStack, Module module) {
        System.err.println("super<Decl>");
    }
}
