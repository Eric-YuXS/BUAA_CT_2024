package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.SymbolType;

import java.util.ArrayList;
import java.util.Collections;

public class Trunc extends Instruction {
    public Trunc(Function function, Instruction truncInstruction) {
        super(function == null ? null : function.getCurBasicBlock(), function == null ? null : function.getNextInstructionName(),
                ValueType.INTEGER, SymbolType.Char, new ArrayList<>(Collections.singletonList(truncInstruction)));
        if (truncInstruction.getValue() != null) {
            this.setValue(truncInstruction.getValue());
        }
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = trunc " + getUses().get(0).toTypeAndNameString()
                + " to i8\n";
    }

    @Override
    public String toNameString() {
        if (getValue() != null) {
            return "" + getValue();
        } else {
            return "%" + getName();
        }
    }

    @Override
    public String toTypeAndNameString() {
        if (getValue() != null) {
            return getSymbolType().toValueString() + " " + getValue();
        } else {
            return getSymbolType().toValueString() + " %" + getName();
        }
    }
}
