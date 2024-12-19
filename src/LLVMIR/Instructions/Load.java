package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.ValueType;
import frontend.SymbolType;

import java.util.ArrayList;
import java.util.Collections;

public class Load extends Instruction {
    public Load(Function function, SymbolType symbolType, Instruction pointerInstruction) {
        super(function.getCurBasicBlock(), function.getNextInstructionName(), ValueType.INTEGER, symbolType,
                new ArrayList<>(Collections.singletonList(pointerInstruction)));
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = load " + getUses().get(0).getSymbolType().arrayOrVarToVar().toValueString()
                + ", " + getUses().get(0).toTypeAndNameString() + "\n";
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
        if (getUses().get(0) instanceof GetElementPointer) {
            return "\tlw $t0, " + ((Instruction) getUses().get(0)).getSpOffset() + "($sp)\n" +
                    "\tlw $t1, 0($t0)\n" +
                    "\tsw $t1, " + getSpOffset() + "($sp)\n";
        } else {
            return "";
        }
    }

    @Override
    public int countMemUse(int count) {
        if (getUses().get(0) instanceof GetElementPointer) {
            setSpOffset(count);
            return count + 4;
        } else {
            setSpOffset(((Instruction) getUses().get(0)).getSpOffset());
            return count;
        }
    }
}
