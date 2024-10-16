package SyntaxTree;

import frontend.Token;

public class ConstInitVal3 extends ConstInitVal {  // ConstInitVal → StringConst
    private final Token stringConst;

    public ConstInitVal3(Token stringConst) {
        super();
        this.stringConst = stringConst;
    }

    @Override
    public String toString() {
        return stringConst + "<ConstInitVal>\n";
    }
}
