package SyntaxTree;

import frontend.Token;

public class BType implements SyntaxTreeNode {  // BType → 'int' | 'char'
    private final Token bType;

    public BType(Token bType) {
        this.bType = bType;
    }

    @Override
    public String toString() {
        return bType.toString();
    }
}
