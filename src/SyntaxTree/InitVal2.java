package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.SymbolStack;
import frontend.Token;

import java.util.ArrayList;

public class InitVal2 extends InitVal {  // InitVal → '{' [ Exp { ',' Exp } ] '}'
    private final Token lBrace;
    private final ArrayList<Exp> exps;
    private final ArrayList<Token> commas;
    private final Token rBrace;

    public InitVal2(Token lBrace, ArrayList<Exp> exps, ArrayList<Token> commas, Token rBrace) {
        super();
        this.lBrace = lBrace;
        this.exps = exps;
        this.commas = commas;
        this.rBrace = rBrace;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lBrace);
        if (!exps.isEmpty()) {
            int expsIndex = 0;
            sb.append(exps.get(expsIndex++));
            for (Token comma : commas) {
                sb.append(comma).append(exps.get(expsIndex++));
            }
        }
        return sb.append(rBrace).append("<InitVal>\n").toString();
    }

    @Override
    public void errorAnalyze(SymbolStack symbolStack) {
        for (Exp exp : exps) {
            exp.errorAnalyze(symbolStack);
        }
    }

    @Override
    public ArrayList<Instruction> analyze(SymbolStack symbolStack, Function function) {
        ArrayList<Instruction> instructions = new ArrayList<>();
        for (Exp exp : exps) {
            instructions.add(exp.analyze(symbolStack, function));
        }
        return instructions;
    }
}
