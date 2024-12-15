package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import frontend.SymbolStack;
import frontend.Token;

import java.util.ArrayList;

public class ConstInitVal2 extends ConstInitVal {  // ConstInitVal → '{' [ ConstExp { ',' ConstExp } ] '}'
    private final Token lBrace;
    private final ArrayList<ConstExp> constExps;
    private final ArrayList<Token> commas;
    private final Token rBrace;

    public ConstInitVal2(Token lBrace, ArrayList<ConstExp> constExps, ArrayList<Token> commas, Token rBrace) {
        super();
        this.lBrace = lBrace;
        this.constExps = constExps;
        this.commas = commas;
        this.rBrace = rBrace;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lBrace);
        if (!constExps.isEmpty()) {
            int constExpsIndex = 0;
            sb.append(constExps.get(constExpsIndex++));
            for (Token comma : commas) {
                sb.append(comma).append(constExps.get(constExpsIndex++));
            }
        }
        return sb.append(rBrace).append("<ConstInitVal>\n").toString();
    }

    public ArrayList<Instruction> analyze(SymbolStack symbolStack, Function function) {
        ArrayList<Instruction> instructions = new ArrayList<>();
        for (ConstExp constExp : constExps) {
            instructions.add(constExp.analyze(symbolStack, function));
        }
        return instructions;
    }
}
