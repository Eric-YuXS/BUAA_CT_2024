package SyntaxTree;

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

    public void analyze(SymbolStack symbolStack) {
        for (LAndExp lAndExp : lAndExps) {
            lAndExp.analyze(symbolStack);
        }
    }
}
