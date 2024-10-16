package SyntaxTree;

import frontend.Token;

import java.util.ArrayList;

public class ConstDecl implements SyntaxTreeNode {  // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
    private final Token constTk;
    private final BType bType;
    private final ArrayList<ConstDef> constDefs;
    private final ArrayList<Token> commas;
    private final Token semicn;

    public ConstDecl(Token constTk, BType bType, ArrayList<ConstDef> constDefs, ArrayList<Token> commas, Token semicn) {
        this.constTk = constTk;
        this.bType = bType;
        this.constDefs = constDefs;
        this.commas = commas;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int constDefsIndex = 0;
        sb.append(constTk).append(bType).append(constDefs.get(constDefsIndex++));
        for (Token comma : commas) {
            sb.append(comma).append(constDefs.get(constDefsIndex++));
        }
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<ConstDecl>\n").toString();
    }
}
