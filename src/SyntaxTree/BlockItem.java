package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
import frontend.FuncSymbol;
import frontend.SymbolStack;

public class BlockItem implements SyntaxTreeNode {  // BlockItem → Decl | Stmt
    @Override
    public String toString() {
        return "super<BlockItem>\n";
    }

    public void analyze(SymbolStack symbolStack, Function function, BasicBlock forCondBasicBlock, BasicBlock forEndBasicBlock) {
        System.err.println("super<BlockItem>");
    }

    public void errorAnalyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean isLoop) {
        System.err.println("super<BlockItem>");
    }

    public boolean errorAnalyzeReturn(SymbolStack symbolStack) {
        System.err.println("super<BlockItem>");
        return false;
    }

    public boolean analyzeReturn(SymbolStack symbolStack) {
        System.err.println("super<BlockItem>");
        return false;
    }

    public void analyzeVoidReturn(Function function) {
        System.err.println("super<BlockItem>");
    }
}
