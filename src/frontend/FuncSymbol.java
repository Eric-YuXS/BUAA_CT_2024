package frontend;

import java.util.ArrayList;

public class FuncSymbol extends Symbol {
    private ArrayList<Symbol> fParams;

    public FuncSymbol(SymbolType symbolType, String symbolName) {
        super(symbolType, symbolName);
    }

    private FuncSymbol(SymbolType symbolType, String symbolName, ArrayList<Symbol> fParams) {
        super(symbolType, symbolName);
        this.fParams = fParams;
    }

    public void setFParams(ArrayList<Symbol> fParams) {
        this.fParams = fParams;
    }

    public ArrayList<Symbol> getFParams() {
        return fParams;
    }

    public static FuncSymbol createMainFuncSymbol() {
        return new FuncSymbol(SymbolType.IntFunc, "main", new ArrayList<>());
    }
}
