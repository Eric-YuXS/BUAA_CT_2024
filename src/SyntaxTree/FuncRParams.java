package SyntaxTree;

import frontend.*;
import frontend.Error;

import java.util.ArrayList;

public class FuncRParams implements SyntaxTreeNode {  // FuncRParams → Exp { ',' Exp }
    private final ArrayList<Exp> exps;
    private final ArrayList<Token> commas;

    public FuncRParams(ArrayList<Exp> exps, ArrayList<Token> commas) {
        this.exps = exps;
        this.commas = commas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int expsIndex = 0;
        sb.append(exps.get(expsIndex++));
        for (Token comma : commas) {
            sb.append(comma).append(exps.get(expsIndex++));
        }
        return sb.append("<FuncRParams>\n").toString();
    }

    public void analyze(SymbolStack symbolStack) {
        for (Exp exp : exps) {
            exp.analyze(symbolStack);
        }
    }

    public void analyze(SymbolStack symbolStack, FuncSymbol funcSymbol, Token ident) {
        ArrayList<Symbol> fParams = funcSymbol.getFParams();
        int fParamsIndex = 0;
        for (Exp exp : exps) {
            SymbolType symbolType = exp.analyze(symbolStack);
            if (fParamsIndex < fParams.size()) {
                if (!fParams.get(fParamsIndex).getSymbolType().canAcceptRParam(symbolType)) {
                    symbolStack.addError(new Error(ident.getLineNumber(), 'e'));
                }
            } else {
                symbolStack.addError(new Error(ident.getLineNumber(), 'd'));
            }
        }
        if (fParamsIndex < fParams.size()) {
            symbolStack.addError(new Error(ident.getLineNumber(), 'd'));
        }
    }
}
