package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;

import java.util.ArrayList;
import java.util.Collections;

public class CallPutChar extends Instruction {
    public CallPutChar(Function function, Instruction varStringInstruction) {
        super(function.getCurBasicBlock(), null, ValueType.VOID, null,
                new ArrayList<>(Collections.singletonList(varStringInstruction)));
    }

    @Override
    public String toString() {
        return "\tcall void @putch(" + getUses().get(0).toTypeAndNameString() + ")\n";
    }
}
