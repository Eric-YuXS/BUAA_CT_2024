package LLVMIR.Instructions;

import LLVMIR.BasicBlock;
import LLVMIR.Instruction;
import LLVMIR.ValueType;

import java.util.ArrayList;
import java.util.Collections;

public class BrUnconditional extends Instruction {
    public BrUnconditional(BasicBlock basicBlock, BasicBlock brBasicBlock) {
        super(basicBlock, null, ValueType.VOID, null, new ArrayList<>(Collections.singletonList(brBasicBlock)));
    }

    public void setBrBasicBlock(BasicBlock brBasicBlock) {
        if (getUses().get(0) == null) {
            getUses().set(0, brBasicBlock);
        }
    }

    @Override
    public String toString() {
        return "\tbr label " + getUses().get(0).toNameString() + "\n";
    }

    @Override
    public String toMips() {
        return "\tj " + ((BasicBlock) getUses().get(0)).getFunction().getName() + getUses().get(0).getName() + "\n";
    }

    @Override
    public int countMemUse(int count) {
        return count;
    }
}
