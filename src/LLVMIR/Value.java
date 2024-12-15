package LLVMIR;

import frontend.SymbolType;

import java.util.ArrayList;

public class Value {
    private String name;
    private final ValueType valueType;
    private final SymbolType symbolType;
    private final ArrayList<Value> uses;
    private Integer value;

    public Value(String name, ValueType valueType, SymbolType symbolType, ArrayList<Value> uses) {
        this.name = name;
        this.valueType = valueType;
        this.symbolType = symbolType;
        this.uses = uses;
        this.value = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void addInstructionUses(ArrayList<Instruction> uses) {
        this.uses.addAll(uses);
    }

    public ArrayList<Value> getUses() {
        return uses;
    }

    @Override
    public String toString() {
        return "Value\n";
    }

    public String toNameString() {
        return "Value\n";
    }

    public String toTypeAndNameString() {
        return "Value\n";
    }
}
