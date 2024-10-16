package SyntaxTree;

import java.util.ArrayList;

public class CompUnit implements SyntaxTreeNode {  // CompUnit → {Decl} {FuncDef} MainFuncDef
    private final ArrayList<Decl> decls;
    private final ArrayList<FuncDef> funcDefs;
    private final MainFuncDef mainFuncDef;

    public CompUnit(ArrayList<Decl> decls, ArrayList<FuncDef> funcDefs, MainFuncDef mainFuncDef) {
        this.decls = decls;
        this.funcDefs = funcDefs;
        this.mainFuncDef = mainFuncDef;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Decl decl : decls) {
            sb.append(decl);
        }
        for (FuncDef funcDef : funcDefs) {
            sb.append(funcDef);
        }
        return sb.append(mainFuncDef).append("<CompUnit>\n").toString();
    }
}
