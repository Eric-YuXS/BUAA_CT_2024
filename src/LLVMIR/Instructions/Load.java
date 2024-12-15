package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.SymbolType;

import java.util.ArrayList;
import java.util.Collections;

public class Load extends Instruction {
    public Load(Function function, SymbolType symbolType, Instruction pointerInstruction) {
        super(function.getCurBasicBlock(), function.getNextInstructionName(), ValueType.INTEGER, symbolType,
                new ArrayList<>(Collections.singletonList(pointerInstruction)));
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = load " + getUses().get(0).getSymbolType().arrayOrVarToVar().toValueString()
                + ", " + getUses().get(0).toTypeAndNameString() + "\n";
    }

    @Override
    public String toNameString() {
        return "%" + getName();
    }

    @Override
    public String toTypeAndNameString() {
        return getSymbolType().toValueString() + " %" + getName();
    }
}
