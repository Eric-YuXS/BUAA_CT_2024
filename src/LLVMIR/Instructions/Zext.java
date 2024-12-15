package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.SymbolType;

import java.util.ArrayList;
import java.util.Collections;

public class Zext extends Instruction {
    public Zext(Function function, Instruction zextInstruction) {
        super(function == null ? null : function.getCurBasicBlock(), function == null ? null : function.getNextInstructionName(),
                ValueType.INTEGER, SymbolType.Int, new ArrayList<>(Collections.singletonList(zextInstruction)));
        if (zextInstruction.getValue() != null) {
            this.setValue(zextInstruction.getValue());
        }
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = zext " + getUses().get(0).toTypeAndNameString()
                + " to i32\n";
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
