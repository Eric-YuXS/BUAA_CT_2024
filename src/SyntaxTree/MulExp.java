package SyntaxTree;

import frontend.Token;

import java.util.ArrayList;

public class MulExp implements SyntaxTreeNode {  // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
    private final ArrayList<UnaryExp> unaryExps;
    private final ArrayList<Token> operators;

    public MulExp(ArrayList<UnaryExp> unaryExps, ArrayList<Token> operators) {
        this.unaryExps = unaryExps;
        this.operators = operators;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int unaryExpsIndex = 0;
        sb.append(unaryExps.get(unaryExpsIndex++));
        for (Token operator : operators) {
            sb.append("<MulExp>\n").append(operator).append(unaryExps.get(unaryExpsIndex++));
        }
        return sb.append("<MulExp>\n").toString();
    }
}
