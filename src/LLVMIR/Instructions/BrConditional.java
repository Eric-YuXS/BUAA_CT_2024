package LLVMIR.Instructions;

import LLVMIR.*;

import java.util.ArrayList;
import java.util.Arrays;

public class BrConditional extends Instruction {
    public BrConditional(BasicBlock basicBlock, Instruction condInstruction, BasicBlock brBasicBlock1, BasicBlock brBasicBlock2) {
        super(basicBlock, null, ValueType.VOID, null,
                new ArrayList<>(Arrays.asList(condInstruction, brBasicBlock1, brBasicBlock2)));
    }

    public void setBrBasicBlock1(BasicBlock brBasicBlock1) {
        if (getUses().get(1) == null) {
            getUses().set(1, brBasicBlock1);
        }
    }

    public void setBrBasicBlock2(BasicBlock brBasicBlock2) {
        if (getUses().get(2) == null) {
            getUses().set(2, brBasicBlock2);
        }
    }

    @Override
    public String toString() {
        return "\tbr " + getUses().get(0).toTypeAndNameString() + ", label " + getUses().get(1).toNameString()
                + ", label " + getUses().get(2).toNameString() + "\n";
    }
}
