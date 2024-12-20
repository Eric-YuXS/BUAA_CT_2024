package LLVMIR;

import LLVMIR.Instructions.*;

import java.util.ArrayList;

public class BasicBlock extends Value {
    private final ArrayList<Instruction> instructions;
    private Function function;

    public BasicBlock(Function function) {
        super(function == null ? null : function.getNextBasicBlockName(), ValueType.LABEL, null, new ArrayList<>());
        instructions = new ArrayList<>();
        this.function = function;
    }

    public void addInstruction(Instruction instruction) {
        if (instruction.getValue() == null || instruction instanceof Ret) {
            instructions.add(instruction);
        }
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        if (this.function == null) {
            this.setName(function.getNextBasicBlockName());
            this.function = function;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(":\n");
        for (Instruction instruction : instructions) {
            sb.append(instruction);
        }
        return sb.toString();
    }

    @Override
    public String toNameString() {
        return "%" + getName();
    }

    @Override
    public String toMips() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFunction().getName()).append(getName()).append(":\n");
        for (Instruction instruction : instructions) {
            sb.append(instruction.toMips());
        }
        return sb.toString();
    }

    public int countMemUse(int count) {
        for (Instruction instruction : instructions) {
            count = instruction.countMemUse(count);
        }
        return count;
    }
}
