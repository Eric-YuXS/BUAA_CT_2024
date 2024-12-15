package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.Icmp;
import LLVMIR.Instructions.Num;
import LLVMIR.Instructions.Sub;
import LLVMIR.Instructions.Zext;
import frontend.SymbolStack;
import frontend.TokenType;

public class UnaryExp3 extends UnaryExp {  // UnaryExp → UnaryOp UnaryExp
    private final UnaryOp unaryOp;
    private final UnaryExp unaryExp;

    public UnaryExp3(UnaryOp unaryOp, UnaryExp unaryExp) {
        super();
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    @Override
    public String toString() {
        return unaryOp.toString() + unaryExp + "<UnaryExp>\n";
    }

    @Override
    public Instruction analyze(SymbolStack symbolStack, Function function) {
        if (unaryOp.getUnaryOp().isType(TokenType.PLUS)) {
            return unaryExp.analyze(symbolStack, function);
        } else if (unaryOp.getUnaryOp().isType(TokenType.MINU)) {
            Sub subInstruction = new Sub(function, Num.getNum(function, 0),
                    unaryExp.analyze(symbolStack, function));
            if (function != null) {
                function.getCurBasicBlock().addInstruction(subInstruction);
            }
            return subInstruction;
        } else if (unaryOp.getUnaryOp().isType(TokenType.NOT)) {
            Instruction notInstruction = new Icmp(function, TokenType.EQL, unaryExp.analyze(symbolStack, function),
                    Num.getNum(function, 0));
            if (function != null) {
                function.getCurBasicBlock().addInstruction(notInstruction);
            }
            notInstruction = new Zext(function, notInstruction);
            if (function != null) {
                function.getCurBasicBlock().addInstruction(notInstruction);
            }
            return notInstruction;
        } else {
            System.err.println(unaryOp.getUnaryOp() + " is not a valid unary operator");
            return null;
        }
    }
}
