package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.SymbolStack;

import java.util.ArrayList;
import java.util.Collections;

public class InitVal1 extends InitVal {  // InitVal → Exp
    private final Exp exp;

    public InitVal1(Exp exp) {
        super();
        this.exp = exp;
    }

    @Override
    public String toString() {
        return exp + "<InitVal>\n";
    }

    @Override
    public ArrayList<Instruction> analyze(SymbolStack symbolStack, Function function) {
        return new ArrayList<>(Collections.singletonList(exp.analyze(symbolStack, function)));
    }
}
