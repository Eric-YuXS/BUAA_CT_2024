package LLVMIR.Instructions;

import LLVMIR.Instruction;
import LLVMIR.Value;
import LLVMIR.ValueType;
import frontend.Symbol;

import java.util.ArrayList;

public class AllocaGlobal extends Instruction {
    private final int size;

    public AllocaGlobal(Symbol symbol, int size, ArrayList<Instruction> valueInstructions) {
        super(null, symbol.getSymbolName(), ValueType.POINTER, symbol.getSymbolType(), new ArrayList<>());
        this.size = size;
        this.addInstructionUses(valueInstructions);
    }

    public int getSize() {
        return size;
    }

    public int getInitValue(int index) {
        if (getUses().size() > index) {
            return getUses().get(index).getValue();
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        ArrayList<Value> uses = getUses();
        if (uses.isEmpty()) {
            return "@" + getName() + " = dso_local global [" + size + " x " + getSymbolType().arrayOrVarToVar().toValueString()
                    + "] zeroinitializer\n";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("@").append(getName()).append(" = dso_local global [").append(size).append(" x ")
                    .append(getSymbolType().arrayOrVarToVar().toValueString()).append("] [")
                    .append(getSymbolType().arrayOrVarToVar().toValueString()).append(" ").append(uses.get(0).toNameString());
            for (int i = 1; i < uses.size(); i++) {
                sb.append(", ").append(getSymbolType().arrayOrVarToVar().toValueString()).append(" ").append(uses.get(i).toNameString());
            }
            for (int i = 0; i < size - uses.size(); i++) {
                sb.append(", ").append(getSymbolType().arrayOrVarToVar().toValueString()).append(" 0");
            }
            return sb.append("]\n").toString();
        }
    }

    @Override
    public String toNameString() {
        return "@" + getName();
    }

    @Override
    public String toTypeAndNameString() {
        return "[" + size + " x " + getSymbolType().arrayOrVarToVar().toValueString() + "]* @" + getName();
    }
}
