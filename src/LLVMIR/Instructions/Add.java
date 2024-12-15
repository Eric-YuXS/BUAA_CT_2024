package LLVMIR.Instructions;

import LLVMIR.*;
import frontend.SymbolType;

import java.util.ArrayList;
import java.util.Arrays;

public class Add extends Instruction {
    public Add(Function function, Instruction addInstruction1, Instruction addInstruction2) {
        super(function == null ? null : function.getCurBasicBlock(), function == null ? null : function.getNextInstructionName(),
                ValueType.INTEGER, SymbolType.Int, new ArrayList<>(Arrays.asList(addInstruction1, addInstruction2)));
        if (addInstruction1.getSymbolType() == SymbolType.VoidFunc || addInstruction2.getSymbolType() == SymbolType.VoidFunc) {
            System.err.println("Calculate with void!");
        }
        if (addInstruction1.getValue() != null && addInstruction2.getValue() != null) {
            this.setValue(addInstruction1.getValue() + addInstruction2.getValue());
        }
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = add " + getUses().get(0).toTypeAndNameString()
                + ", " + getUses().get(1).toNameString() + "\n";
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
