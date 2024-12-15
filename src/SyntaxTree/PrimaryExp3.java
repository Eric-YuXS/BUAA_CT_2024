package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.Num;
import frontend.SymbolStack;
import frontend.SymbolType;

public class PrimaryExp3 extends PrimaryExp {  // PrimaryExp → Number
    private final Number number;

    public PrimaryExp3(Number number) {
        super();
        this.number = number;
    }

    @Override
    public String toString() {
        return number + "<PrimaryExp>\n";
    }

    public Instruction analyze(SymbolStack symbolStack, Function function) {
        return new Num(function, SymbolType.Int, number.getNum());
    }
}
