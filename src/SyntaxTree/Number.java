package SyntaxTree;

import frontend.Token;

public class Number implements SyntaxTreeNode {  // Number → IntConst
    private final Token intConst;

    public Number(Token intConst) {
        this.intConst = intConst;
    }

    @Override
    public String toString() {
        return intConst + "<Number>\n";
    }
}
