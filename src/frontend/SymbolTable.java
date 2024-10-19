package frontend;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    private final HashMap<String, Symbol> symbolTable;
    private final ArrayList<Symbol> symbols;
    private static int totalNum = 0;
    private static final ArrayList<SymbolTable> symbolTables = new ArrayList<>();
    private final int tableNum;

    public SymbolTable() {
        symbolTable = new HashMap<>();
        symbols = new ArrayList<>();
        tableNum = ++totalNum;
        symbolTables.add(this);
    }

    public Symbol getSymbol(String symbolName) {
        return symbolTable.getOrDefault(symbolName, null);
    }

    public void addSymbol(Symbol symbol) {
        if (symbolTable.containsKey(symbol.getSymbolName())) {
            System.err.println("Symbol " + symbol.getSymbolName() + " already exists");
        } else {
            symbolTable.put(symbol.getSymbolName(), symbol);
            symbols.add(symbol);
        }
    }

    public static ArrayList<SymbolTable> getSymbolTables() {
        return symbolTables;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Symbol symbol : symbols) {
            sb.append(tableNum).append(" ").append(symbol);
        }
        return sb.toString();
    }
}
