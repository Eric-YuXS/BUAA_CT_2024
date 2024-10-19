package SyntaxTree;

import frontend.SymbolStack;

public class Decl1 extends Decl {  // Decl → ConstDecl
    private final ConstDecl constDecl;

    public Decl1(ConstDecl constDecl) {
        super();
        this.constDecl = constDecl;
    }

    @Override
    public String toString() {
        return constDecl.toString();
    }

    @Override
    public void analyze(SymbolStack symbolStack) {
        constDecl.analyze(symbolStack);
    }
}
