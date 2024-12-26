package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.SymbolStack;
import frontend.SymbolType;

public class Exp implements SyntaxTreeNode {  // Exp → AddExp
    private final AddExp addExp;

    public Exp(AddExp addExp) {
        this.addExp = addExp;
    }

    @Override
    public String toString() {
        return addExp + "<Exp>\n";
    }

    public SymbolType errorAnalyze(SymbolStack symbolStack) {
        return addExp.errorAnalyze(symbolStack);
    }

    public Instruction analyze(SymbolStack symbolStack, Function function) {
        return addExp.analyze(symbolStack, function);
    }
}
