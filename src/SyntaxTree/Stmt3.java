package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
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
    public void analyze(SymbolStack symbolStack, Function function, BasicBlock forCondBasicBlock, BasicBlock forEndBasicBlock) {
        block.analyze(symbolStack, function, false, forCondBasicBlock, forEndBasicBlock);
    }
}
