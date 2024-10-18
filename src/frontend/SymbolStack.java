package frontend;

import java.util.ArrayList;
import java.util.Stack;

public class SymbolStack {
    private final Stack<SymbolTable> symbolStack;
    private int scopeNum;
    private final ArrayList<Error> errors;

    public SymbolStack() {
        symbolStack = new Stack<>();
        errors = new ArrayList<>();
        symbolStack.push(new SymbolTable());
    }

    public void enterScope() {
        symbolStack.push(new SymbolTable());
    }

    public void exitScope() {
        symbolStack.pop();
    }

    public Symbol getSymbol(String symbolName) {
        for (int i = symbolStack.size() - 1; i >= 0; i--) {
            SymbolTable currentScope = symbolStack.get(i);
            Symbol symbol = currentScope.getSymbol(symbolName);
            if (symbol != null) {
                return symbol;
            }
        }
        return null;
    }

    public boolean addSymbol(Symbol symbol) {
        SymbolTable currentScope = symbolStack.peek();
        if (currentScope.getSymbol(symbol.getSymbolName()) == null) {
            currentScope.addSymbol(symbol);
            return true;
        } else {
            return false;
        }
    }

    public void addError(Error error) {
        errors.add(error);
    }

    public ArrayList<Error> getErrors() {
        return errors;
    }
}
