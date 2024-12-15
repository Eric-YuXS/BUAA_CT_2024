package frontend;

import LLVMIR.Function;

import java.util.ArrayList;

public class FuncSymbol extends Symbol {
    private ArrayList<Symbol> fParams;
    private Function function;

    public FuncSymbol(SymbolType symbolType, String symbolName) {
        super(symbolType, symbolName, null);
    }

    private FuncSymbol(SymbolType symbolType, String symbolName, ArrayList<Symbol> fParams) {
        super(symbolType, symbolName, null);
        this.fParams = fParams;
    }

    public void setFParams(ArrayList<Symbol> fParams) {
        this.fParams = fParams;
    }

    public ArrayList<Symbol> getFParams() {
        return fParams;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }

    public String toValueString() {
        return getSymbolType().toValueString() + " @" + getSymbolName();
    }

    public static FuncSymbol createMainFuncSymbol() {
        return new FuncSymbol(SymbolType.IntFunc, "main", new ArrayList<>());
    }
}
