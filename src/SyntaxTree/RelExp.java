package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.Icmp;
import LLVMIR.Instructions.Zext;
import frontend.SymbolStack;
import frontend.Token;

import java.util.ArrayList;

public class RelExp implements SyntaxTreeNode {  // RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
    private final ArrayList<AddExp> addExps;
    private final ArrayList<Token> operators;

    public RelExp(ArrayList<AddExp> addExps, ArrayList<Token> operators) {
        this.addExps = addExps;
        this.operators = operators;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int addExpsIndex = 0;
        sb.append(addExps.get(addExpsIndex++));
        for (Token operator : operators) {
            sb.append("<RelExp>\n").append(operator).append(addExps.get(addExpsIndex++));
        }
        return sb.append("<RelExp>\n").toString();
    }

    public Instruction analyze(SymbolStack symbolStack, Function function) {
        Instruction relInstruction = addExps.get(0).analyze(symbolStack, function);
        for (int i = 0; i < operators.size(); i++) {
            AddExp addExp = addExps.get(i + 1);
            Instruction addInstruction = addExp.analyze(symbolStack, function);
            relInstruction = new Icmp(function, operators.get(i).getType(), relInstruction, addInstruction);
            function.getCurBasicBlock().addInstruction(relInstruction);
            if (i < operators.size() - 1) {
                relInstruction = new Zext(function, relInstruction);
                function.getCurBasicBlock().addInstruction(relInstruction);
            }
        }
        return relInstruction;
    }
}
