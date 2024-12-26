package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.SymbolStack;

import java.util.ArrayList;
import java.util.Collections;

public class ConstInitVal1 extends ConstInitVal {  // ConstInitVal → ConstExp
    private final ConstExp constExp;

    public ConstInitVal1(ConstExp constExp) {
        super();
        this.constExp = constExp;
    }

    @Override
    public String toString() {
        return constExp + "<ConstInitVal>\n";
    }

    @Override
    public void errorAnalyze(SymbolStack symbolStack) {
        constExp.errorAnalyze(symbolStack);
    }

    @Override
    public ArrayList<Instruction> analyze(SymbolStack symbolStack, Function function) {
        return new ArrayList<>(Collections.singletonList(constExp.analyze(symbolStack, function)));
    }
}
