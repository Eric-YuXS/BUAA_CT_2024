package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.SymbolStack;
import frontend.SymbolType;

public class PrimaryExp implements SyntaxTreeNode {  // PrimaryExp → '(' Exp ')' | LVal | Number | Character
    @Override
    public String toString() {
        return "super<PrimaryExp>\n";
    }

    public SymbolType errorAnalyze(SymbolStack symbolStack) {
        System.err.println("super<PrimaryExp>");
        return null;
    }

    public Instruction analyze(SymbolStack symbolStack, Function function) {
        System.err.println("super<PrimaryExp>");
        return null;
    }
}
