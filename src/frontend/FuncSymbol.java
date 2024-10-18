package frontend;

import java.util.ArrayList;

public class FuncSymbol extends Symbol {
    private ArrayList<Symbol> fParams;

    public FuncSymbol(SymbolType symbolType, String symbolName) {
        super(symbolType, symbolName);
    }

    public void setFParams(ArrayList<Symbol> fParams) {
        this.fParams = fParams;
    }

    public ArrayList<Symbol> getFParams() {
        return fParams;
    }
}
