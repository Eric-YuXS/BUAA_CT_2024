package SyntaxTree;

import LLVMIR.Module;
import frontend.SymbolStack;

public class Decl2 extends Decl {  // Decl → VarDecl
    private final VarDecl varDecl;

    public Decl2(VarDecl varDecl) {
        super();
        this.varDecl = varDecl;
    }

    @Override
    public String toString() {
        return varDecl.toString();
    }

    @Override
    public void analyze(SymbolStack symbolStack, Module module) {
        varDecl.analyze(symbolStack, module);
    }
}
