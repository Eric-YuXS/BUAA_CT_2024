package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.SymbolType;

import java.util.ArrayList;

public class Num extends Instruction {
    public Num(Function function, SymbolType symbolType, int value) {
        super(function == null ? null : function.getCurBasicBlock(), null, ValueType.INTEGER, symbolType, new ArrayList<>());
        this.setValue(value);
    }

    public static Num getNum(Function function, int num) {
        return new Num(function, SymbolType.Int, num);
    }

    public static Num getChar(Function function, int charNum) {
        return new Num(function, SymbolType.Char, charNum);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String toNameString() {
        if (getValue() != null) {
            return "" + getValue();
        } else {
            return "%" + getName();
        }
    }

    @Override
    public String toTypeAndNameString() {
        return getSymbolType().toValueString() + " " + getValue();
    }
}
