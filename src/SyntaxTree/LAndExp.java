package SyntaxTree;

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
}
