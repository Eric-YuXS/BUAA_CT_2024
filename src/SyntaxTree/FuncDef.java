package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Module;
import frontend.*;
import frontend.Error;

import java.util.ArrayList;

public class FuncDef implements SyntaxTreeNode {  // FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
    private final FuncType funcType;
    private final Token ident;
    private final Token lParent;
    private final FuncFParams funcFParams;
    private final Token rParent;
    private final Block block;

    public FuncDef(FuncType funcType, Token ident, Token lParent, FuncFParams funcFParams, Token rParent, Block block) {
        this.funcType = funcType;
        this.ident = ident;
        this.lParent = lParent;
        this.funcFParams = funcFParams;
        this.rParent = rParent;
        this.block = block;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(funcType).append(ident).append(lParent);
        if (funcFParams != null) {
            sb.append(funcFParams);
        }
        if (rParent != null) {
            sb.append(rParent);
        }
        return sb.append(block).append("<FuncDef>\n").toString();
    }

    public void errorAnalyze(SymbolStack symbolStack) {
        SymbolType symbolType = funcType.getSymbolType();
        FuncSymbol funcSymbol = new FuncSymbol(symbolType, ident.getString());
        if (!symbolStack.addSymbol(funcSymbol)) {
            symbolStack.addError(new Error(ident.getLineNumber(), 'b'));
        }
        if (funcFParams != null) {
            symbolStack.enterScope();
            funcSymbol.setFParams(funcFParams.errorAnalyze(symbolStack));
            block.errorAnalyze(symbolStack, funcSymbol, true, false);
        } else {
            funcSymbol.setFParams(new ArrayList<>());
            block.errorAnalyze(symbolStack, funcSymbol, false, false);
        }
        if (symbolType == SymbolType.IntFunc || symbolType == SymbolType.CharFunc) {
            block.errorAnalyzeReturn(symbolStack);
        }
    }

    public Function analyze(SymbolStack symbolStack, Module module) {
        SymbolType symbolType = funcType.getSymbolType();
        FuncSymbol funcSymbol = new FuncSymbol(symbolType, ident.getString());
        Function function = new Function(module, funcSymbol);
        funcSymbol.setFunction(function);
        module.addFunction(function);
        if (!symbolStack.addSymbol(funcSymbol)) {
            symbolStack.addError(new Error(ident.getLineNumber(), 'b'));
        }
        if (funcFParams != null) {
            symbolStack.enterScope();
            funcSymbol.setFParams(funcFParams.analyze(symbolStack, function));
            block.analyze(symbolStack, function, true, null, null);
        } else {
            funcSymbol.setFParams(new ArrayList<>());
            block.analyze(symbolStack, function, false, null, null);
        }
        if (symbolType == SymbolType.IntFunc || symbolType == SymbolType.CharFunc) {
            block.analyzeReturn(symbolStack);
        } else {
            block.analyzeVoidReturn(function);
        }
        return function;
    }
}
