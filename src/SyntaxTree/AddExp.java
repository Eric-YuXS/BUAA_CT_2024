package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.Add;
import LLVMIR.Instructions.Sub;
import frontend.SymbolStack;
import frontend.SymbolType;
import frontend.Token;
import frontend.TokenType;

import java.util.ArrayList;

public class AddExp implements SyntaxTreeNode {  // AddExp → MulExp | AddExp ('+' | '−') MulExp
    private final ArrayList<MulExp> mulExps;
    private final ArrayList<Token> operators;

    public AddExp(ArrayList<MulExp> mulExps, ArrayList<Token> operators) {
        this.mulExps = mulExps;
        this.operators = operators;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int mulExpsIndex = 0;
        sb.append(mulExps.get(mulExpsIndex++));
        for (Token operator : operators) {
            sb.append("<AddExp>\n").append(operator).append(mulExps.get(mulExpsIndex++));
        }
        return sb.append("<AddExp>\n").toString();
    }

    public SymbolType errorAnalyze(SymbolStack symbolStack) {
        if (mulExps.size() == 1) {
            return mulExps.get(0).errorAnalyze(symbolStack);
        } else {
            for (MulExp mulExp : mulExps) {
                if (mulExp.errorAnalyze(symbolStack) == SymbolType.VoidFunc) {
                    System.err.println("Calculate with void!");
                    return null;
                }
            }
            return SymbolType.Int;
        }
    }

    public Instruction analyze(SymbolStack symbolStack, Function function) {
        Instruction addInstruction = mulExps.get(0).analyze(symbolStack, function);
        for (int i = 0; i < operators.size(); i++) {
            MulExp mulExp = mulExps.get(i + 1);
            Instruction mulInstruction = mulExp.analyze(symbolStack, function);
            if (operators.get(i).isType(TokenType.PLUS)) {
                addInstruction = new Add(function, addInstruction, mulInstruction);
            } else if (operators.get(i).isType(TokenType.MINU)) {
                addInstruction = new Sub(function, addInstruction, mulInstruction);
            } else {
                System.err.println("Unrecognized operator: " + operators.get(i));
            }
            if (function != null) {
                function.getCurBasicBlock().addInstruction(addInstruction);
            }
        }
        return addInstruction;
    }
}
