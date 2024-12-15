package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.Symbol;

import java.util.ArrayList;

public class ParamVar extends Instruction {
    public ParamVar(Function function, Symbol symbol) {
        super(function.getCurBasicBlock(), function.getNextInstructionName(), ValueType.POINTER, symbol.getSymbolType(),
                new ArrayList<>());
    }

    @Override
    public String toNameString() {
        return "%" + getName();
    }

    @Override
    public String toTypeAndNameString() {
        return getSymbolType().toValueString() + " %" + getName();
    }
}
