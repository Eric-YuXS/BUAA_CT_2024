package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.Symbol;

import java.util.ArrayList;

public class ParamArray extends Instruction {
    public ParamArray(Function function, Symbol symbol) {
        super(null, function.getNextInstructionName(), ValueType.POINTER, symbol.getSymbolType(), new ArrayList<>());
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
