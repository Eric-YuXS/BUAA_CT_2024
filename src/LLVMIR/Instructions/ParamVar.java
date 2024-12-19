package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.Symbol;

import java.util.ArrayList;

public class ParamVar extends Instruction {
    private final int paramIndex;

    public ParamVar(Function function, Symbol symbol, int paramIndex) {
        super(function.getCurBasicBlock(), function.getNextInstructionName(), ValueType.POINTER, symbol.getSymbolType(),
                new ArrayList<>());
        this.paramIndex = paramIndex;
    }

    @Override
    public String toNameString() {
        return "%" + getName();
    }

    @Override
    public String toTypeAndNameString() {
        return getSymbolType().toValueString() + " %" + getName();
    }

    @Override
    public String toMips() {
        if (paramIndex < 4) {
            return "\tsw $a" + paramIndex + ", " + getSpOffset() + "($sp)\n";
        } else {
            return "\tlw $t0, " + (paramIndex - 4) * 4 + "($fp)\n" +
                    "\tsw $t0, " + getSpOffset() + "($sp)\n";
        }
    }

    @Override
    public int countMemUse(int count) {
        setSpOffset(count);
        return count + 4;
    }
}
