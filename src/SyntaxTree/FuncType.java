package SyntaxTree;

import frontend.SymbolType;
import frontend.Token;

import java.util.Objects;

public class FuncType implements SyntaxTreeNode {  // FuncType → 'void' | 'int' | 'char'
    private final Token funcType;

    public FuncType(Token funcType) {
        this.funcType = funcType;
    }

    @Override
    public String toString() {
        return funcType + "<FuncType>\n";
    }

    public SymbolType getSymbolType() {
        return Objects.requireNonNull(SymbolType.TokenToSymbolType(funcType)).varOrVoidToFunc();
    }
}
