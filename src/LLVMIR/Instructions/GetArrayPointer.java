package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.SymbolType;

import java.util.ArrayList;
import java.util.Arrays;

public class GetArrayPointer extends Instruction {
    private final int size;

    public GetArrayPointer(Function function, SymbolType symbolType, Instruction pointerInstruction, int size) {
        super(function.getCurBasicBlock(), function.getNextInstructionName(), ValueType.POINTER, symbolType,
                new ArrayList<>(Arrays.asList(pointerInstruction, Num.getNum(function, 0))));
        this.size = size;
    }

    @Override
    public String toString() {
        if (size == -1) {
            return "\t%" + getName() + " = getelementptr inbounds " + getSymbolType().arrayOrVarToVar().toValueString()
                    + ", " + getUses().get(0).toTypeAndNameString() + ", " + getUses().get(1).toTypeAndNameString() + "\n";
        } else {
            return "\t%" + getName() + " = getelementptr inbounds [" + size + " x " + getSymbolType().arrayOrVarToVar().toValueString()
                    + "], " + getUses().get(0).toTypeAndNameString() + ", i32 0, " + getUses().get(1).toTypeAndNameString() + "\n";
        }
    }

    @Override
    public String toNameString() {
        return "%" + getName();
    }

    @Override
    public String toTypeAndNameString() {
        return getSymbolType().toValueString() + " %" + getName();
    }

    @Override
    public String toMips() {
        return "";
    }

    @Override
    public int countMemUse(int count) {
        return count;
    }
}
