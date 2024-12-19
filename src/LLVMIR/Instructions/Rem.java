package LLVMIR.Instructions;

import LLVMIR.*;
import frontend.SymbolType;

import java.util.ArrayList;
import java.util.Arrays;

public class Rem extends Instruction {
    public Rem(Function function, Instruction remInstruction1, Instruction remInstruction2) {
        super(function == null ? null : function.getCurBasicBlock(), function == null ? null : function.getNextInstructionName(),
                ValueType.INTEGER, SymbolType.Int, new ArrayList<>(Arrays.asList(remInstruction1, remInstruction2)));
        if (remInstruction1.getSymbolType() == SymbolType.VoidFunc || remInstruction2.getSymbolType() == SymbolType.VoidFunc) {
            System.err.println("Calculate with void!");
        }
        if (remInstruction1.getValue() != null && remInstruction2.getValue() != null) {
            this.setValue(remInstruction1.getValue() % remInstruction2.getValue());
        }
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = srem " + getUses().get(0).toTypeAndNameString()
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
                        "\tdiv $t0, $t1\n" +
                        "\tmfhi $t0\n" +
                        "\tsw $t0, " + getSpOffset() + "($sp)\n";
            } else if (uses.get(1).getValue() != null) {
                return "\tlw $t0, " + ((Instruction) uses.get(0)).getSpOffset() + "($sp)\n" +
                        "\tlw $t1, " + uses.get(1).getValue() + "\n" +
                        "\tdiv $t0, $t1\n" +
                        "\tmfhi $t0\n" +
                        "\tsw $t0, " + getSpOffset() + "($sp)\n";
            } else {
                return "\tlw $t0, " + ((Instruction) uses.get(0)).getSpOffset() + "($sp)\n" +
                        "\tlw $t1, " + ((Instruction) uses.get(1)).getSpOffset() + "($sp)\n" +
                        "\tdiv $t0, $t1\n" +
                        "\tmfhi $t0\n" +
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
