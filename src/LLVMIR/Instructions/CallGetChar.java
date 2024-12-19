package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.SymbolType;

import java.util.ArrayList;

public class CallGetChar extends Instruction {
    public CallGetChar(Function function) {
        super(function.getCurBasicBlock(), function.getNextInstructionName(), ValueType.INTEGER, SymbolType.Int, new ArrayList<>());
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = call i32 @getchar()\n";
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

    @Override
    public String toMips() {
        return "\tli $v0, 12\n" +
                "\tsyscall\n" +
                "\tsw $v0, " + getSpOffset() + "($sp)\n";
    }

    @Override
    public int countMemUse(int count) {
        setSpOffset(count);
        return count + 4;
    }
}
