package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
import LLVMIR.Instructions.Ret;
import frontend.SymbolStack;

public class BlockItem1 extends BlockItem {  // BlockItem → Decl
    private final Decl decl;

    public BlockItem1(Decl decl) {
        super();
        this.decl = decl;
    }

    @Override
    public String toString() {
        return decl.toString();
    }

    @Override
    public void analyze(SymbolStack symbolStack, Function function, BasicBlock forCondBasicBlock, BasicBlock forEndBasicBlock) {
        decl.analyze(symbolStack, function.getModule());
    }

    @Override
    public boolean analyzeReturn(SymbolStack symbolStack) {
        return false;
    }

    public void analyzeVoidReturn(Function function) {
        function.getCurBasicBlock().addInstruction(new Ret(function, null));
    }
}
