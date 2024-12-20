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

    @Override
    public String toMips() {
        ArrayList<Value> uses = getUses();
        if (!uses.isEmpty()) {
            if (uses.get(0).getValue() == null) {
                return "\tlw $v0, " + ((Instruction) uses.get(0)).getSpOffset() + "($sp)\n";
            } else {
                return "\tli $v0, " + uses.get(0).getValue() + "\n";
            }
        } else {
            return "";
        }
    }

    @Override
    public int countMemUse(int count) {
        return count;
    }
}
