package SyntaxTree;

import frontend.Token;

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
}
