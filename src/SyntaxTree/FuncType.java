package SyntaxTree;

import frontend.Token;

public class FuncType implements SyntaxTreeNode {  // FuncType → 'void' | 'int' | 'char'
    private final Token funcType;

    public FuncType(Token funcType) {
        this.funcType = funcType;
    }

    @Override
    public String toString() {
        return funcType + "<FuncType>\n";
    }
}
