package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Module;
import frontend.FuncSymbol;
import frontend.SymbolStack;
import frontend.Token;

public class MainFuncDef implements SyntaxTreeNode {  // MainFuncDef → 'int' 'main' '(' ')' Block
    private final Token intTk;
    private final Token main;
    private final Token lParent;
    private final Token rParent;
    private final Block block;

    public MainFuncDef(Token intTk, Token main, Token lParent, Token rParent, Block block) {
        this.intTk = intTk;
        this.main = main;
        this.lParent = lParent;
        this.rParent = rParent;
        this.block = block;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(intTk).append(main).append(lParent);
        if (rParent != null) {
            sb.append(rParent);
        }
        return sb.append(block).append("<MainFuncDef>\n").toString();
    }

    public Function analyze(SymbolStack symbolStack, Module module) {
        Function function = new Function(module, FuncSymbol.createMainFuncSymbol());
        module.addFunction(function);
        block.analyze(symbolStack, function, false, null, null);
        block.analyzeReturn(symbolStack);
        return function;
    }
}
