package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.SymbolType;

import java.util.ArrayList;
import java.util.Arrays;

public class GetElementPointer extends Instruction {
    private final int size;

    public GetElementPointer(Function function, SymbolType symbolType, Instruction pointerInstruction,
                             Instruction indexInstruction, int size) {
        super(function.getCurBasicBlock(), function.getNextInstructionName(), ValueType.POINTER, symbolType.arrayOrVarToVar(),
                new ArrayList<>(Arrays.asList(pointerInstruction, indexInstruction)));
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
        return getSymbolType().toValueString() + "* %" + getName();
    }
}
