package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.SymbolStack;

public class ConstExp implements SyntaxTreeNode {  // ConstExp → AddExp
    private final AddExp addExp;

    public ConstExp(AddExp addExp) {
        this.addExp = addExp;
    }

    @Override
    public String toString() {
        return addExp + "<ConstExp>\n";
    }

    public Instruction analyze(SymbolStack symbolStack, Function function) {
        return addExp.analyze(symbolStack, function);
    }
}
