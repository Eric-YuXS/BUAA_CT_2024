package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;

import java.util.ArrayList;
import java.util.Collections;

public class CallPutStr extends Instruction {
    public CallPutStr(Function function, Instruction allocaStringInstruction) {
        super(function.getCurBasicBlock(), null, ValueType.VOID, null,
                new ArrayList<>(Collections.singletonList(allocaStringInstruction)));
    }

    @Override
    public String toString() {
        AllocaPrintString allocaPrintStringInstruction = (AllocaPrintString) getUses().get(0);
        int size = allocaPrintStringInstruction.getSize();
        return "\tcall void @putstr(i8* getelementptr inbounds ([" + size + " x i8], " + allocaPrintStringInstruction.toTypeAndNameString()
                + ", i64 0, i64 0))\n";
    }

    @Override
    public String toMips() {
        return "\tla $a0, " + getUses().get(0).getName() + "\n" +
                "\tli $v0, 4\n" +
                "\tsyscall\n";
    }

    @Override
    public int countMemUse(int count) {
        return count;
    }
}
