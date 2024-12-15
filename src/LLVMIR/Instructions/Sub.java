package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.SymbolType;

import java.util.ArrayList;
import java.util.Arrays;

public class Sub extends Instruction {
    public Sub(Function function, Instruction subInstruction1, Instruction subInstruction2) {
        super(function == null ? null : function.getCurBasicBlock(), function == null ? null : function.getNextInstructionName(),
                ValueType.INTEGER, SymbolType.Int, new ArrayList<>(Arrays.asList(subInstruction1, subInstruction2)));
        if (subInstruction1.getSymbolType() == SymbolType.VoidFunc || subInstruction2.getSymbolType() == SymbolType.VoidFunc) {
            System.err.println("Calculate with void!");
        }
        if (subInstruction1.getValue() != null && subInstruction2.getValue() != null) {
            this.setValue(subInstruction1.getValue() - subInstruction2.getValue());
        }
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = sub " + getUses().get(0).toTypeAndNameString()
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
