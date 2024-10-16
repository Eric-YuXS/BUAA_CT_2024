package SyntaxTree;

import frontend.Token;

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
}
