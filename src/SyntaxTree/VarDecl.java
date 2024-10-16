package SyntaxTree;

import frontend.Token;

import java.util.ArrayList;

public class VarDecl implements SyntaxTreeNode {  // VarDecl → BType VarDef { ',' VarDef } ';'
    private final BType bType;
    private final ArrayList<VarDef> varDefs;
    private final ArrayList<Token> commas;
    private final Token semicn;

    public VarDecl(BType bType, ArrayList<VarDef> varDefs, ArrayList<Token> commas, Token semicn) {
        this.bType = bType;
        this.varDefs = varDefs;
        this.commas = commas;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int varDefsIndex = 0;
        sb.append(bType).append(varDefs.get(varDefsIndex++));
        for (Token comma : commas) {
            sb.append(comma).append(varDefs.get(varDefsIndex++));
        }
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<VarDecl>\n").toString();
    }
}
