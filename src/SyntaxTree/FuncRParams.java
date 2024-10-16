package SyntaxTree;

import frontend.Token;

import java.util.ArrayList;

public class FuncRParams implements SyntaxTreeNode {  // FuncRParams → Exp { ',' Exp }
    private final ArrayList<Exp> exps;
    private final ArrayList<Token> commas;

    public FuncRParams(ArrayList<Exp> exps, ArrayList<Token> commas) {
        this.exps = exps;
        this.commas = commas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int expsIndex = 0;
        sb.append(exps.get(expsIndex++));
        for (Token comma : commas) {
            sb.append(comma).append(exps.get(expsIndex++));
        }
        return sb.append("<FuncRParams>\n").toString();
    }
}
