package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.*;
import frontend.SymbolStack;
import frontend.Token;
import frontend.TokenType;

import java.util.ArrayList;

public class MulExp implements SyntaxTreeNode {  // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
    private final ArrayList<UnaryExp> unaryExps;
    private final ArrayList<Token> operators;

    public MulExp(ArrayList<UnaryExp> unaryExps, ArrayList<Token> operators) {
        this.unaryExps = unaryExps;
        this.operators = operators;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int unaryExpsIndex = 0;
        sb.append(unaryExps.get(unaryExpsIndex++));
        for (Token operator : operators) {
            sb.append("<MulExp>\n").append(operator).append(unaryExps.get(unaryExpsIndex++));
        }
        return sb.append("<MulExp>\n").toString();
    }

    public Instruction analyze(SymbolStack symbolStack, Function function) {
        Instruction mulInstruction = unaryExps.get(0).analyze(symbolStack, function);
        if (mulInstruction.getSymbolType().isI8()) {
            mulInstruction = new Zext(function, mulInstruction);
            if (function != null) {
                function.getCurBasicBlock().addInstruction(mulInstruction);
            }
        }
        for (int i = 0; i < operators.size(); i++) {
            UnaryExp unaryExp = unaryExps.get(i + 1);
            Instruction unaryInstruction = unaryExp.analyze(symbolStack, function);
            if (unaryInstruction.getSymbolType().isI8()) {
                unaryInstruction = new Zext(function, unaryInstruction);
                if (function != null) {
                    function.getCurBasicBlock().addInstruction(unaryInstruction);
                }
            }
            if (operators.get(i).isType(TokenType.MULT)) {
                mulInstruction = new Mul(function, mulInstruction, unaryInstruction);
            } else if (operators.get(i).isType(TokenType.DIV)) {
                mulInstruction = new Div(function, mulInstruction, unaryInstruction);
            } else if (operators.get(i).isType(TokenType.MOD)) {
                mulInstruction = new Rem(function, mulInstruction, unaryInstruction);
            } else {
                System.err.println("Unrecognized operator: " + operators.get(i));
            }
            if (function != null) {
                function.getCurBasicBlock().addInstruction(mulInstruction);
            }
        }
        return mulInstruction;
    }
}
