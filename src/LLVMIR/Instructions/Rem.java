package LLVMIR.Instructions;

import LLVMIR.*;
import frontend.SymbolType;

import java.util.ArrayList;
import java.util.Arrays;

public class Rem extends Instruction {
    public Rem(Function function, Instruction remInstruction1, Instruction remInstruction2) {
        super(function == null ? null : function.getCurBasicBlock(), function == null ? null : function.getNextInstructionName(),
                ValueType.INTEGER, SymbolType.Int, new ArrayList<>(Arrays.asList(remInstruction1, remInstruction2)));
        if (remInstruction1.getSymbolType() == SymbolType.VoidFunc || remInstruction2.getSymbolType() == SymbolType.VoidFunc) {
            System.err.println("Calculate with void!");
        }
        if (remInstruction1.getValue() != null && remInstruction2.getValue() != null) {
            this.setValue(remInstruction1.getValue() % remInstruction2.getValue());
        }
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = srem " + getUses().get(0).toTypeAndNameString()
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
