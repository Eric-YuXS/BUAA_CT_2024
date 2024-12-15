package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.SymbolType;

import java.util.ArrayList;

public class CallGetInt extends Instruction {
    public CallGetInt(Function function) {
        super(function.getCurBasicBlock(), function.getNextInstructionName(), ValueType.INTEGER, SymbolType.Int, new ArrayList<>());
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = call i32 @getint()\n";
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
        return getSymbolType().toValueString() + " %" + getName();
    }
}
