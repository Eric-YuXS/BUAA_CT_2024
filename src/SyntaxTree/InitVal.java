package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.SymbolStack;

import java.util.ArrayList;

public class InitVal implements SyntaxTreeNode {  // InitVal → Exp | '{' [ Exp { ',' Exp } ] '}' | StringConst
    @Override
    public String toString() {
        return "super<InitVal>\n";
    }

    public ArrayList<Instruction> analyze(SymbolStack symbolStack, Function function) {
        System.err.println("super<InitVal>");
        return null;
    }
}
