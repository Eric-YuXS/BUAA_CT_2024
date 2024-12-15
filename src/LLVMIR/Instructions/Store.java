package LLVMIR.Instructions;

import LLVMIR.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Store extends Instruction {
    public Store(Function function, Instruction pointerInstruction, Instruction valueInstruction) {
        super(function.getCurBasicBlock(), null, ValueType.VOID, null,
                new ArrayList<>(Arrays.asList(pointerInstruction, valueInstruction)));
    }

    @Override
    public String toString() {
        ArrayList<Value> uses = getUses();
        return "\tstore " + uses.get(1).toTypeAndNameString() + ", " + uses.get(0).toTypeAndNameString() + "\n";
    }
}
