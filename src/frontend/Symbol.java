package frontend;

public class Symbol {
    private final String symbolName;
    private final SymbolType symbolType;

    public Symbol(SymbolType symbolType, String symbolName) {
        this.symbolType = symbolType;
        this.symbolName = symbolName;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    @Override
    public String toString() {
        return symbolName + " " + symbolType + "\n";
    }
}
