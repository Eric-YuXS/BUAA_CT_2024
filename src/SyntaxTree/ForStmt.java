package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.Store;
import LLVMIR.Instructions.Trunc;
import frontend.SymbolStack;
import frontend.Token;

public class ForStmt implements SyntaxTreeNode {  // ForStmt → LVal '=' Exp
    private final LVal lVal;
    private final Token assign;
    private final Exp exp;

    public ForStmt(LVal lVal, Token assign, Exp exp) {
        this.lVal = lVal;
        this.assign = assign;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return lVal.toString() + assign + exp + "<ForStmt>\n";
    }

    public void errorAnalyze(SymbolStack symbolStack) {
        lVal.errorAnalyze(symbolStack, true);
        exp.errorAnalyze(symbolStack);
    }

    public void analyze(SymbolStack symbolStack, Function function) {
        Instruction lvalInstruction = lVal.analyze(symbolStack, function, true);
        Instruction expInstruction = exp.analyze(symbolStack, function);
        if (lvalInstruction.getSymbolType().isI8()) {
            expInstruction = new Trunc(function, expInstruction);
            function.getCurBasicBlock().addInstruction(expInstruction);
        }
        function.getCurBasicBlock().addInstruction(new Store(function, lvalInstruction, expInstruction));
    }
}
