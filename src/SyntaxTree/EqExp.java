package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.Icmp;
import LLVMIR.Instructions.Num;
import LLVMIR.Instructions.Zext;
import frontend.SymbolStack;
import frontend.Token;
import frontend.TokenType;

import java.util.ArrayList;

public class EqExp implements SyntaxTreeNode {  // EqExp → RelExp | EqExp ('==' | '!=') RelExp
    private final ArrayList<RelExp> relExps;
    private final ArrayList<Token> operators;

    public EqExp(ArrayList<RelExp> relExps, ArrayList<Token> operators) {
        this.relExps = relExps;
        this.operators = operators;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int relExpsIndex = 0;
        sb.append(relExps.get(relExpsIndex++));
        for (Token operator : operators) {
            sb.append("<EqExp>\n").append(operator).append(relExps.get(relExpsIndex++));
        }
        return sb.append("<EqExp>\n").toString();
    }

    public void errorAnalyze(SymbolStack symbolStack) {
        for (RelExp relExp : relExps) {
            relExp.errorAnalyze(symbolStack);
        }
    }

    public Instruction analyze(SymbolStack symbolStack, Function function) {
        Instruction eqInstruction = relExps.get(0).analyze(symbolStack, function);
        if (operators.size() > 1 && eqInstruction.getSymbolType().isI1()) {
            eqInstruction = new Zext(function, eqInstruction);
            function.getCurBasicBlock().addInstruction(eqInstruction);
        }
        for (int i = 0; i < operators.size(); i++) {
            RelExp relExp = relExps.get(i + 1);
            Instruction relInstruction = relExp.analyze(symbolStack, function);
            if (relInstruction.getSymbolType().isI1()) {
                relInstruction = new Zext(function, relInstruction);
                function.getCurBasicBlock().addInstruction(relInstruction);
            }
            eqInstruction = new Icmp(function, operators.get(i).getType(), eqInstruction, relInstruction);
            function.getCurBasicBlock().addInstruction(eqInstruction);
            if (i < operators.size() - 1) {
                eqInstruction = new Zext(function, eqInstruction);
                function.getCurBasicBlock().addInstruction(eqInstruction);
            }
        }
        if (eqInstruction.getSymbolType().isI32()) {
            eqInstruction = new Icmp(function, TokenType.NEQ, eqInstruction, Num.getNum(function, 0));
            function.getCurBasicBlock().addInstruction(eqInstruction);
        }
        return eqInstruction;
    }
}
