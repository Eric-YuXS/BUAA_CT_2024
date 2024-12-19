package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.SymbolType;

import java.util.ArrayList;

public class Alloca extends Instruction {
    private final int size;

    public Alloca(Function function, SymbolType symbolType, int size) {
        super(function.getCurBasicBlock(), function.getNextInstructionName(), ValueType.POINTER, symbolType, new ArrayList<>());
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = alloca [" + size + " x " + getSymbolType().arrayOrVarToVar().toValueString() + "]\n";
    }

    @Override
    public String toNameString() {
        return "%" + getName();
    }

    @Override
    public String toTypeAndNameString() {
        return "[" + size + " x " + getSymbolType().arrayOrVarToVar().toValueString() + "]* %" + getName();
    }

    @Override
    public String toMips() {
        return "";
    }

    @Override
    public int countMemUse(int count) {
        setSpOffset(count);
        return count + 4 * size;
    }
}
