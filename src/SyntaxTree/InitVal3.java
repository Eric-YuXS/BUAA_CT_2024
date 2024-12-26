package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.SymbolStack;
import frontend.Token;

import java.util.ArrayList;

public class InitVal3 extends InitVal {  // InitVal → StringConst
    private final Token stringConst;

    public InitVal3(Token stringConst) {
        super();
        this.stringConst = stringConst;
    }

    @Override
    public String toString() {
        return stringConst + "<InitVal>\n";
    }

    @Override
    public void errorAnalyze(SymbolStack symbolStack) {
    }

    @Override
    public ArrayList<Instruction> analyze(SymbolStack symbolStack, Function function) {
        return null;
    }

    public String getString() {
        return stringConst.getString();
    }
}
