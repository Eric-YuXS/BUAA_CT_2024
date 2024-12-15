package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.SymbolType;

import java.util.ArrayList;

public class Var extends Instruction {
    public Var(Function function, SymbolType symbolType) {
        super(function.getCurBasicBlock(), function.getNextInstructionName(), ValueType.POINTER, symbolType, new ArrayList<>());
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = alloca " + getSymbolType().toValueString() + "\n";
    }

    @Override
    public String toNameString() {
        return "%" + getName();
    }

    @Override
    public String toTypeAndNameString() {
        return getSymbolType().toValueString() + "* %" + getName();
    }
}
