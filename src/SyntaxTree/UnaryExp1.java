package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.SymbolStack;
import frontend.SymbolType;

public class UnaryExp1 extends UnaryExp {  // UnaryExp → PrimaryExp
    private final PrimaryExp primaryExp;

    public UnaryExp1(PrimaryExp primaryExp) {
        super();
        this.primaryExp = primaryExp;
    }

    @Override
    public String toString() {
        return primaryExp + "<UnaryExp>\n";
    }

    @Override
    public SymbolType errorAnalyze(SymbolStack symbolStack) {
        return primaryExp.errorAnalyze(symbolStack);
    }

    @Override
    public Instruction analyze(SymbolStack symbolStack, Function function) {
        return primaryExp.analyze(symbolStack, function);
    }
}
