package SyntaxTree;

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

    public SymbolType analyze(SymbolStack symbolStack) {
        return SymbolType.Char;
    }
}
