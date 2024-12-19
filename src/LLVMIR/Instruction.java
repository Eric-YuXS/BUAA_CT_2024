package LLVMIR;

import frontend.SymbolType;

import java.util.ArrayList;

public class Instruction extends Value {
    private final BasicBlock basicBlock;
    private int spOffset;

    public Instruction(BasicBlock basicBlock, String name, ValueType valueType, SymbolType symbolType, ArrayList<Value> uses) {
        super(name, valueType, symbolType, uses);
        this.basicBlock = basicBlock;
    }

    public Function getFunction() {
        return basicBlock.getFunction();
    }

    @Override
    public String toString() {
        return "Instruction\n";
    }

    @Override
    public String toMips() {
        return "Instruction\n";
    }

    public int getSpOffset() {
        return spOffset;
    }

    public void setSpOffset(int spOffset) {
        this.spOffset = spOffset;
    }

    public int countMemUse(int count) {
        System.err.println("Instruction\n");
        return -1;
    }
}
