package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.*;

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

    public ArrayList<SymbolType> errorAnalyze(SymbolStack symbolStack) {
        ArrayList<SymbolType> symbolTypes = new ArrayList<>();
        for (Exp exp : exps) {
            symbolTypes.add(exp.errorAnalyze(symbolStack));
        }
        return symbolTypes;
    }

    public ArrayList<Instruction> analyze(SymbolStack symbolStack, Function function) {
        ArrayList<Instruction> instructions = new ArrayList<>();
        for (Exp exp : exps) {
            instructions.add(exp.analyze(symbolStack, function));
        }
        return instructions;
    }
}
