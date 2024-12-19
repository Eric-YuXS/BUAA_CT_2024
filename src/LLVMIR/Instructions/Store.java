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

    @Override
    public String toMips() {
        ArrayList<Value> uses = getUses();
        if (uses.get(1).getValue() == null) {
            return "\tlw $t0, " + ((Instruction) uses.get(1)).getSpOffset() + "($sp)\n" +
                    "\tsw $t0, " + ((Instruction) uses.get(0)).getSpOffset() + "($sp)\n";
        } else {
            return "\tli $t0, " + uses.get(1).getValue() + "\n" +
                    "\tsw $t0, " + ((Instruction) uses.get(0)).getSpOffset() + "($sp)\n";
        }
    }

    @Override
    public int countMemUse(int count) {
        return count;
    }
}
