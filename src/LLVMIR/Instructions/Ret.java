package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Value;

import java.util.ArrayList;
import java.util.Collections;

public class Ret extends Instruction {
    public Ret(Function function, Instruction returnInstruction) {
        super(function.getCurBasicBlock(), null, function.getValueType(), function.getSymbolType(),
                returnInstruction != null ? new ArrayList<>(Collections.singletonList(returnInstruction)) : new ArrayList<>());
        if (returnInstruction != null && returnInstruction.getValue() != null) {
            this.setValue(returnInstruction.getValue());
        }
    }

    @Override
    public String toString() {
        ArrayList<Value> uses = getUses();
        if (!uses.isEmpty()) {
            return "\tret " + uses.get(0).toTypeAndNameString() + "\n";
        } else {
            return "\tret void\n";
        }
    }
}
