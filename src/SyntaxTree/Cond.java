package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instructions.BrConditional;
import frontend.SymbolStack;

import java.util.ArrayList;

public class Cond implements SyntaxTreeNode {  // Cond → LOrExp
    private final LOrExp lOrExp;

    public Cond(LOrExp lOrExp) {
        this.lOrExp = lOrExp;
    }

    @Override
    public String toString() {
        return lOrExp + "<Cond>\n";
    }

    public ArrayList<BrConditional> analyze(SymbolStack symbolStack, Function function) {
        return lOrExp.analyze(symbolStack, function);
    }
}
