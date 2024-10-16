package SyntaxTree;

import frontend.Token;

public class FuncFParam implements SyntaxTreeNode {  // FuncFParam → BType Ident ['[' ']']
    private final BType bType;
    private final Token ident;
    private final Token lBrack;
    private final Token rBrack;

    public FuncFParam(BType bType, Token ident, Token lBrack, Token rBrack) {
        this.bType = bType;
        this.ident = ident;
        this.lBrack = lBrack;
        this.rBrack = rBrack;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(bType).append(ident);
        if (lBrack != null) {
            sb.append(lBrack);
            if (rBrack != null) {
                sb.append(rBrack);
            }
        }
        return sb.append("<FuncFParam>\n").toString();
    }
}
