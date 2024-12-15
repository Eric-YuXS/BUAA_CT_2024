package frontend;

import LLVMIR.Instruction;

public class Symbol {
    private final String symbolName;
    private final SymbolType symbolType;
    private Instruction instruction;

    public Symbol(SymbolType symbolType, String symbolName) {
        this.symbolType = symbolType;
        this.symbolName = symbolName;
    }

    public Symbol(SymbolType symbolType, String symbolName, Instruction instruction) {
        this.symbolType = symbolType;
        this.symbolName = symbolName;
        this.instruction = instruction;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public String toValueString() {
        return symbolType.toValueString() + " %" + symbolName;
    }

    @Override
    public String toString() {
        return symbolName + " " + symbolType + "\n";
    }
}
