package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.Load;
import frontend.SymbolStack;
import frontend.SymbolType;

public class PrimaryExp2 extends PrimaryExp {  // PrimaryExp → LVal
    private final LVal lVal;

    public PrimaryExp2(LVal lVal) {
        super();
        this.lVal = lVal;
    }

    @Override
    public String toString() {
        return lVal + "<PrimaryExp>\n";
    }

    public SymbolType errorAnalyze(SymbolStack symbolStack) {
        return lVal.errorAnalyze(symbolStack, false);
    }

    public Instruction analyze(SymbolStack symbolStack, Function function) {
        Instruction pointerInstruction = lVal.analyze(symbolStack, function, false);
        if (function == null) {
            return pointerInstruction;
        } else if (pointerInstruction.getSymbolType().isVar()) {
            Load loadInstruction = new Load(function, pointerInstruction.getSymbolType(), pointerInstruction);
            function.getCurBasicBlock().addInstruction(loadInstruction);
            return loadInstruction;
        } else {
            return pointerInstruction;
        }
    }
}
