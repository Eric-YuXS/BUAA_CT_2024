package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.SymbolStack;

public class UnaryExp implements SyntaxTreeNode {  // UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    @Override
    public String toString() {
        return "super<UnaryExp>\n";
    }

    public Instruction analyze(SymbolStack symbolStack, Function function) {
        System.err.println("super<UnaryExp>");
        return null;
    }
}
