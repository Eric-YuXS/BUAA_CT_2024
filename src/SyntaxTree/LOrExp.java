package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instructions.BrConditional;
import frontend.SymbolStack;
import frontend.Token;

import java.util.ArrayList;

public class LOrExp implements SyntaxTreeNode {  // LOrExp → LAndExp | LOrExp '||' LAndExp
    private final ArrayList<LAndExp> lAndExps;
    private final ArrayList<Token> operators;

    public LOrExp(ArrayList<LAndExp> lAndExps, ArrayList<Token> operators) {
        this.lAndExps = lAndExps;
        this.operators = operators;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int lAndExpsIndex = 0;
        sb.append(lAndExps.get(lAndExpsIndex++));
        for (Token operator : operators) {
            sb.append("<LOrExp>\n").append(operator).append(lAndExps.get(lAndExpsIndex++));
        }
        return sb.append("<LOrExp>\n").toString();
    }

    public void errorAnalyze(SymbolStack symbolStack) {
        for (LAndExp lAndExp : lAndExps) {
            lAndExp.errorAnalyze(symbolStack);
        }
    }

    public ArrayList<BrConditional> analyze(SymbolStack symbolStack, Function function) {
        ArrayList<BrConditional> lAndBrInstructions = lAndExps.get(0).analyze(symbolStack, function);
        ArrayList<BrConditional> lOrBrInstructions = new ArrayList<>();
        for (int i = 0; i < operators.size(); i++) {
            function.enterNextBasicBlock();
            for (BrConditional brConditional : lAndBrInstructions) {
                brConditional.setBrBasicBlock2(function.getCurBasicBlock());
            }
            lOrBrInstructions.add(lAndBrInstructions.get(lAndBrInstructions.size() - 1));
            lAndBrInstructions = lAndExps.get(i + 1).analyze(symbolStack, function);
        }
        lOrBrInstructions.addAll(lAndBrInstructions);
        return lOrBrInstructions;
    }
}
