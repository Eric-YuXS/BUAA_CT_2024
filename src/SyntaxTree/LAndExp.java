package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.BrConditional;
import frontend.SymbolStack;
import frontend.Token;

import java.util.ArrayList;

public class LAndExp implements SyntaxTreeNode {  // LAndExp → EqExp | LAndExp '&&' EqExp
    private final ArrayList<EqExp> eqExps;
    private final ArrayList<Token> operators;

    public LAndExp(ArrayList<EqExp> eqExps, ArrayList<Token> operators) {
        this.eqExps = eqExps;
        this.operators = operators;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int eqExpsIndex = 0;
        sb.append(eqExps.get(eqExpsIndex++));
        for (Token operator : operators) {
            sb.append("<LAndExp>\n").append(operator).append(eqExps.get(eqExpsIndex++));
        }
        return sb.append("<LAndExp>\n").toString();
    }

    public ArrayList<BrConditional> analyze(SymbolStack symbolStack, Function function) {
        Instruction lAndInstruction = eqExps.get(0).analyze(symbolStack, function);
        ArrayList<BrConditional> lAndBrInstructions = new ArrayList<>();
        for (int i = 0; i < operators.size(); i++) {
            BasicBlock lastBasicBlock = function.getCurBasicBlock();
            function.enterNextBasicBlock();
            BrConditional brInstruction = new BrConditional(lastBasicBlock, lAndInstruction, function.getCurBasicBlock(), null);
            lastBasicBlock.addInstruction(brInstruction);
            lAndBrInstructions.add(brInstruction);
            lAndInstruction = eqExps.get(i + 1).analyze(symbolStack, function);
        }
        BrConditional lAndBrInstruction = new BrConditional(function.getCurBasicBlock(), lAndInstruction, null, null);
        function.getCurBasicBlock().addInstruction(lAndBrInstruction);
        lAndBrInstructions.add(lAndBrInstruction);
        return lAndBrInstructions;
    }
}
