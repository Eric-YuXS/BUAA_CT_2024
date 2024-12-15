package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.SymbolStack;

public class PrimaryExp implements SyntaxTreeNode {  // PrimaryExp → '(' Exp ')' | LVal | Number | Character
    @Override
    public String toString() {
        return "super<PrimaryExp>\n";
    }

    public Instruction analyze(SymbolStack symbolStack, Function function) {
        System.err.println("super<PrimaryExp>");
        return null;
    }
}
