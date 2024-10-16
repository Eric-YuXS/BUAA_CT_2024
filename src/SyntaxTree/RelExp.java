package SyntaxTree;

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
}
