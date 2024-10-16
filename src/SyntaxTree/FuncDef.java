package SyntaxTree;

import frontend.Token;

public class FuncDef implements SyntaxTreeNode {  // FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
    private final FuncType funcType;
    private final Token ident;
    private final Token lParent;
    private final FuncFParams funcFParams;
    private final Token rParent;
    private final Block block;

    public FuncDef(FuncType funcType, Token ident, Token lParent, FuncFParams funcFParams, Token rParent, Block block) {
        this.funcType = funcType;
        this.ident = ident;
        this.lParent = lParent;
        this.funcFParams = funcFParams;
        this.rParent = rParent;
        this.block = block;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(funcType).append(ident).append(lParent);
        if (funcFParams != null) {
            sb.append(funcFParams);
        }
        if (rParent != null) {
            sb.append(rParent);
        }
        return sb.append(block).append("<FuncDef>\n").toString();
    }
}
