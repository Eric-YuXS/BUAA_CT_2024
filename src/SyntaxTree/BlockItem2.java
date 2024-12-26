package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
import LLVMIR.Instructions.Ret;
import frontend.FuncSymbol;
import frontend.SymbolStack;

public class BlockItem2 extends BlockItem {  // BlockItem → Stmt
    private final Stmt stmt;

    public BlockItem2(Stmt stmt) {
        super();
        this.stmt = stmt;
    }

    @Override
    public String toString() {
        return stmt.toString();
    }

    @Override
    public void errorAnalyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean isLoop) {
        stmt.errorAnalyze(symbolStack, funcSymbol, isLoop);
    }

    @Override
    public boolean errorAnalyzeReturn(SymbolStack symbolStack) {
        return stmt instanceof Stmt8;
    }

    @Override
    public void analyze(SymbolStack symbolStack, Function function, BasicBlock forCondBasicBlock, BasicBlock forEndBasicBlock) {
        stmt.analyze(symbolStack, function, forCondBasicBlock, forEndBasicBlock);
    }

    @Override
    public boolean analyzeReturn(SymbolStack symbolStack) {
        return stmt instanceof Stmt8;
    }

    public void analyzeVoidReturn(Function function) {
        if (!(stmt instanceof Stmt8)) {
            function.getCurBasicBlock().addInstruction(new Ret(function, null));
        }
    }
}
