package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.SymbolStack;
import frontend.Token;

import java.util.ArrayList;

public class ConstInitVal3 extends ConstInitVal {  // ConstInitVal → StringConst
    private final Token stringConst;

    public ConstInitVal3(Token stringConst) {
        super();
        this.stringConst = stringConst;
    }

    @Override
    public String toString() {
        return stringConst + "<ConstInitVal>\n";
    }

    public void errorAnalyze(SymbolStack symbolStack) {
    }

    public ArrayList<Instruction> analyze(SymbolStack symbolStack, Function function) {
        return null;
    }

    public String getString() {
        return stringConst.getString();
    }
}
