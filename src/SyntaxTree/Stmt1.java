package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.Store;
import LLVMIR.Instructions.Trunc;
import frontend.FuncSymbol;
import frontend.SymbolStack;
import frontend.Token;

public class Stmt1 extends Stmt {  // Stmt → LVal '=' Exp ';'
    private final LVal lVal;
    private final Token assign;
    private final Exp exp;
    private final Token semicn;

    public Stmt1(LVal lVal, Token assign, Exp exp, Token semicn) {
        super();
        this.lVal = lVal;
        this.assign = assign;
        this.exp = exp;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lVal).append(assign).append(exp);
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<Stmt>\n").toString();
    }

    @Override
    public void errorAnalyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean isLoop) {
        lVal.errorAnalyze(symbolStack, true);
        exp.errorAnalyze(symbolStack);
    }

    @Override
    public void analyze(SymbolStack symbolStack, Function function, BasicBlock forCondBasicBlock, BasicBlock forEndBasicBlock) {
        Instruction lvalInstruction = lVal.analyze(symbolStack, function, true);
        Instruction expInstruction = exp.analyze(symbolStack, function);
        if (lvalInstruction.getSymbolType().isI8()) {
            expInstruction = new Trunc(function, expInstruction);
            function.getCurBasicBlock().addInstruction(expInstruction);
        }
        function.getCurBasicBlock().addInstruction(new Store(function, lvalInstruction, expInstruction));
    }
}
