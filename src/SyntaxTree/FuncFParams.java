package SyntaxTree;

import LLVMIR.Function;
import frontend.Symbol;
import frontend.SymbolStack;
import frontend.Token;

import java.util.ArrayList;

public class FuncFParams implements SyntaxTreeNode {  // FuncFParams → FuncFParam { ',' FuncFParam }
    private final ArrayList<FuncFParam> funcFParams;
    private final ArrayList<Token> commas;

    public FuncFParams(ArrayList<FuncFParam> funcFParams, ArrayList<Token> commas) {
        this.funcFParams = funcFParams;
        this.commas = commas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int funcFParamsIndex = 0;
        sb.append(funcFParams.get(funcFParamsIndex++));
        for (Token comma : commas) {
            sb.append(comma).append(funcFParams.get(funcFParamsIndex++));
        }
        return sb.append("<FuncFParams>\n").toString();
    }

    public ArrayList<Symbol> analyze(SymbolStack symbolStack, Function function) {
        ArrayList<Symbol> symbols = new ArrayList<>();
        for (int i = 0; i < funcFParams.size(); i++) {
            symbols.add(funcFParams.get(i).analyze(symbolStack, function, i));
        }
        return symbols;
    }
}
