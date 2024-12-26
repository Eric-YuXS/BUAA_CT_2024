package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.Num;
import frontend.SymbolStack;
import frontend.SymbolType;

public class PrimaryExp4 extends PrimaryExp {  // PrimaryExp → Character
    private final Character character;

    public PrimaryExp4(Character character) {
        super();
        this.character = character;
    }

    @Override
    public String toString() {
        return character + "<PrimaryExp>\n";
    }

    public SymbolType errorAnalyze(SymbolStack symbolStack) {
        return SymbolType.Char;
    }

    public Instruction analyze(SymbolStack symbolStack, Function function) {
        return new Num(function, SymbolType.Char, character.getNum());
    }
}
