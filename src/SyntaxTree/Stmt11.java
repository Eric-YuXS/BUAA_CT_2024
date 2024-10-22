package SyntaxTree;

import frontend.*;
import frontend.Error;

import java.util.ArrayList;

public class Stmt11 extends Stmt {  // Stmt → 'printf''('StringConst {','Exp}')'';'
    private final Token printf;
    private final Token lParent;
    private final Token stringConst;
    private final String[] strings;
    private final ArrayList<Exp> exps;
    private final ArrayList<Token> commas;
    private final Token rParent;
    private final Token semicn;

    public Stmt11(Token printf, Token lParent, Token stringConst, ArrayList<Exp> exps,
                  ArrayList<Token> commas, Token rParent, Token semicn) {
        super();
        this.printf = printf;
        this.lParent = lParent;
        this.stringConst = stringConst;
        strings = stringConst.getString().split("%d|%c");
        this.exps = exps;
        this.commas = commas;
        this.rParent = rParent;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(printf).append(lParent).append(stringConst);
        int expIndex = 0;
        for (Token comma : commas) {
            sb.append(comma).append(exps.get(expIndex++));
        }
        if (rParent != null) {
            sb.append(rParent);
        }
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<Stmt>\n").toString();
    }

    @Override
    public void analyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean isLoop) {
        if (strings.length - 1 != exps.size()) {
            symbolStack.addError(new Error(printf.getLineNumber(), 'l'));
        }
        for (Exp exp : exps) {
            if (exp.analyze(symbolStack) == SymbolType.VoidFunc) {
                System.err.println("Print void!");
            }
        }
    }
}
