package LLVMIR.Instructions;

import LLVMIR.*;
import frontend.SymbolType;

import java.util.ArrayList;
import java.util.Arrays;

public class Div extends Instruction {
    public Div(Function function, Instruction divInstruction1, Instruction divInstruction2) {
        super(function == null ? null : function.getCurBasicBlock(), function == null ? null : function.getNextInstructionName(),
                ValueType.INTEGER, SymbolType.Int, new ArrayList<>(Arrays.asList(divInstruction1, divInstruction2)));
        if (divInstruction1.getSymbolType() == SymbolType.VoidFunc || divInstruction2.getSymbolType() == SymbolType.VoidFunc) {
            System.err.println("Calculate with void!");
        }
        if (divInstruction1.getValue() != null && divInstruction2.getValue() != null) {
            this.setValue(divInstruction1.getValue() / divInstruction2.getValue());
        }
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = sdiv " + getUses().get(0).toTypeAndNameString()
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
