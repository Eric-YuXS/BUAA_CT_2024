package LLVMIR.Instructions;

import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.Symbol;

import java.util.ArrayList;
import java.util.Collections;

public class VarGlobal extends Instruction {
    public VarGlobal(Symbol symbol, Instruction valueInstruction) {
        super(null, symbol.getSymbolName(), ValueType.INTEGER, symbol.getSymbolType(),
                new ArrayList<>(Collections.singletonList(valueInstruction)));
    }

    @Override
    public String toString() {
        return "@" + getName() + " = dso_local global " + getSymbolType().toValueString() + " " +
                (getUses().get(0) == null ? "0" : getUses().get(0).toNameString()) + "\n";
    }

    @Override
    public String toNameString() {
        return "@" + getName();
    }

    @Override
    public String toTypeAndNameString() {
        return getSymbolType().toValueString() + "* @" + getName();
    }

    @Override
    public String toMips() {
        return getName() + (getSymbolType().isI32() ?  ":\t.word " : ":\t.byte ") +
                (getUses().get(0) == null ? "0" : getUses().get(0).getValue()) + "\n";
    }
}
