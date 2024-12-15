package LLVMIR;

import frontend.SymbolType;

import java.util.ArrayList;

public class Instruction extends Value {
    private final BasicBlock basicBlock;

    public Instruction(BasicBlock basicBlock, String name, ValueType valueType, SymbolType symbolType, ArrayList<Value> uses) {
        super(name, valueType, symbolType, uses);
        this.basicBlock = basicBlock;
    }

    @Override
    public String toString() {
        return "Instruction\n";
    }
}
