package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.Load;
import frontend.SymbolStack;

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

    public Instruction analyze(SymbolStack symbolStack, Function function) {
        Instruction pointerInstruction = lVal.analyze(symbolStack, function, false);
        if (pointerInstruction.getSymbolType().isVar()) {
            Load loadInstruction = new Load(function, pointerInstruction.getSymbolType(), pointerInstruction);
            function.getCurBasicBlock().addInstruction(loadInstruction);
            return loadInstruction;
        } else {
            return pointerInstruction;
        }
    }
}
