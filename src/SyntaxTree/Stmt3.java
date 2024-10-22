package SyntaxTree;

import frontend.FuncSymbol;
import frontend.SymbolStack;

public class Stmt3 extends Stmt {  // Stmt → Block
    private final Block block;

    public Stmt3(Block block) {
        super();
        this.block = block;
    }

    @Override
    public String toString() {
        return block + "<Stmt>\n";
    }

    @Override
    public void analyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean isLoop) {
        block.analyze(symbolStack, funcSymbol, false, isLoop);
    }
}
