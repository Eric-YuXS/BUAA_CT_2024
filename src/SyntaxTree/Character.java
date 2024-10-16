package SyntaxTree;

import frontend.Token;

public class Character implements SyntaxTreeNode {  // Character → CharConst
    private final Token charConst;

    public Character(Token charConst) {
        this.charConst = charConst;
    }

    @Override
    public String toString() {
        return charConst + "<Character>\n";
    }
}
