package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
import LLVMIR.Instructions.Ret;
import frontend.Error;
import frontend.FuncSymbol;
import frontend.SymbolStack;
import frontend.Token;

import java.util.ArrayList;

public class Block implements SyntaxTreeNode {  // Block → '{' { BlockItem } '}'
    private final Token lBrace;
    private final ArrayList<BlockItem> blockItems;
    private final Token rBrace;

    public Block(Token lBrace, ArrayList<BlockItem> blockItems, Token rBrace) {
        this.lBrace = lBrace;
        this.blockItems = blockItems;
        this.rBrace = rBrace;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lBrace);
        for (BlockItem blockItem : blockItems) {
            sb.append(blockItem);
        }
        return sb.append(rBrace).append("<Block>\n").toString();
    }

    public void errorAnalyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean hasEnteredScope, boolean isLoop) {
        if (!hasEnteredScope) {
            symbolStack.enterScope();
        }
        for (BlockItem blockItem : blockItems) {
            blockItem.errorAnalyze(symbolStack, funcSymbol, isLoop);
        }
        symbolStack.exitScope();
    }

    public void errorAnalyzeReturn(SymbolStack symbolStack) {
        if (blockItems.isEmpty() || !blockItems.get(blockItems.size() - 1).errorAnalyzeReturn(symbolStack)) {
            symbolStack.addError(new Error(rBrace.getLineNumber(), 'g'));
        }
    }

    public void analyze(SymbolStack symbolStack, Function function, boolean hasEnteredScope,
                        BasicBlock forCondBasicBlock, BasicBlock forEndBasicBlock) {
        if (!hasEnteredScope) {
            symbolStack.enterScope();
        }
        for (BlockItem blockItem : blockItems) {
            blockItem.analyze(symbolStack, function, forCondBasicBlock, forEndBasicBlock);
        }
        symbolStack.exitScope();
    }

    public void analyzeReturn(SymbolStack symbolStack) {
        if (blockItems.isEmpty() || !blockItems.get(blockItems.size() - 1).analyzeReturn(symbolStack)) {
            symbolStack.addError(new Error(rBrace.getLineNumber(), 'g'));
        }
    }

    public void analyzeVoidReturn(Function function) {
        if (blockItems.isEmpty()) {
            function.getCurBasicBlock().addInstruction(new Ret(function, null));
        } else {
            blockItems.get(blockItems.size() - 1).analyzeVoidReturn(function);
        }
    }
}
