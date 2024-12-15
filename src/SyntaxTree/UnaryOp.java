package SyntaxTree;

import frontend.Token;

public class UnaryOp implements SyntaxTreeNode {  // UnaryOp → '+' | '−' | '!'
    private final Token unaryOp;

    public UnaryOp(Token unaryOp) {
        this.unaryOp = unaryOp;
    }

    public Token getUnaryOp() {
        return unaryOp;
    }

    @Override
    public String toString() {
        return unaryOp + "<UnaryOp>\n";
    }
}
