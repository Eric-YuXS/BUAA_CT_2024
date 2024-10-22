package SyntaxTree;

import frontend.SymbolStack;
import frontend.Token;

public class InitVal3 extends InitVal {  // InitVal → StringConst
    private final Token stringConst;

    public InitVal3(Token stringConst) {
        super();
        this.stringConst = stringConst;
    }

    @Override
    public String toString() {
        return stringConst + "<InitVal>\n";
    }

    @Override
    public void analyze(SymbolStack symbolStack) {
    }
}
