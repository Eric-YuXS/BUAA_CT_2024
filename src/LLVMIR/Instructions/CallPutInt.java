package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;

import java.util.ArrayList;
import java.util.Collections;

public class CallPutInt extends Instruction {
    public CallPutInt(Function function, Instruction varStringInstruction) {
        super(function.getCurBasicBlock(), null, ValueType.VOID, null,
                new ArrayList<>(Collections.singletonList(varStringInstruction)));
    }

    @Override
    public String toString() {
        return "\tcall void @putint(" + getUses().get(0).toTypeAndNameString() + ")\n";
    }

    @Override
    public String toMips() {
        return "\tlw $a0, " + ((Instruction) getUses().get(0)).getSpOffset() + "($sp)\n" +
                "\tli $v0, 1\n" +
                "\tsyscall\n";
    }

    @Override
    public int countMemUse(int count) {
        return count;
    }
}
