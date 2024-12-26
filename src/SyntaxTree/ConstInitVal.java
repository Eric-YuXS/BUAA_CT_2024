package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.SymbolStack;

import java.util.ArrayList;

public class ConstInitVal implements SyntaxTreeNode {  // ConstInitVal → ConstExp | '{' [ ConstExp { ',' ConstExp } ] '}' | StringConst
    @Override
    public String toString() {
        return "super<ConstInitVal>\n";
    }

    public void errorAnalyze(SymbolStack symbolStack) {
        System.out.println("super<ConstInitVal>");
    }

    public ArrayList<Instruction> analyze(SymbolStack symbolStack, Function function) {
        System.out.println("super<ConstInitVal>");
        return null;
    }
}
