package LLVMIR.Instructions;

import LLVMIR.*;
import frontend.SymbolType;

import java.util.ArrayList;
import java.util.Arrays;

public class Mul extends Instruction {
    public Mul(Function function, Instruction mulInstruction1, Instruction mulInstruction2) {
        super(function == null ? null : function.getCurBasicBlock(), function == null ? null : function.getNextInstructionName(),
                ValueType.INTEGER, SymbolType.Int, new ArrayList<>(Arrays.asList(mulInstruction1, mulInstruction2)));
        if (mulInstruction1.getSymbolType() == SymbolType.VoidFunc || mulInstruction2.getSymbolType() == SymbolType.VoidFunc) {
            System.err.println("Calculate with void!");
        }
        if (mulInstruction1.getValue() != null && mulInstruction2.getValue() != null) {
            this.setValue(mulInstruction1.getValue() * mulInstruction2.getValue());
        }
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = mul " + getUses().get(0).toTypeAndNameString()
                + ", " + getUses().get(1).toNameString() + "\n";
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
        if (getValue() != null) {
            return getSymbolType().toValueString() + " " + getValue();
        } else {
            return getSymbolType().toValueString() + " %" + getName();
        }
    }

    @Override
    public String toMips() {
        ArrayList<Value> uses = getUses();
        if (getValue() == null) {
            if (uses.get(0).getValue() != null) {
                return "\tli $t0, " + uses.get(0).getValue() + "\n" +
                        "\tlw $t1, " + ((Instruction) uses.get(1)).getSpOffset() + "($sp)\n" +
                        "\tmult $t0, $t1\n" +
                        "\tmflo $t0\n" +
                        "\tsw $t0, " + getSpOffset() + "($sp)\n";
            } else if (uses.get(1).getValue() != null) {
                return "\tlw $t0, " + ((Instruction) uses.get(0)).getSpOffset() + "($sp)\n" +
                        "\tlw $t1, " + uses.get(1).getValue() + "\n" +
                        "\tmult $t0, $t1\n" +
                        "\tmflo $t0\n" +
                        "\tsw $t0, " + getSpOffset() + "($sp)\n";
            } else {
                return "\tlw $t0, " + ((Instruction) uses.get(0)).getSpOffset() + "($sp)\n" +
                        "\tlw $t1, " + ((Instruction) uses.get(1)).getSpOffset() + "($sp)\n" +
                        "\tmult $t0, $t1\n" +
                        "\tmflo $t0\n" +
                        "\tsw $t0, " + getSpOffset() + "($sp)\n";
            }
        } else {
            return "\tli $t0, " + getValue() + "\n" +
                    "\tsw $t0, " + getSpOffset() + "($sp)\n";
        }
    }

    @Override
    public int countMemUse(int count) {
        setSpOffset(count);
        return count + 4;
    }
}
