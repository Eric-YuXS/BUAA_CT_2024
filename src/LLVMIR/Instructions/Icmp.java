package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Value;
import LLVMIR.ValueType;
import frontend.SymbolType;
import frontend.TokenType;

import java.util.ArrayList;
import java.util.Arrays;

public class Icmp extends Instruction {
    private final TokenType cmpType;

    public Icmp(Function function, TokenType cmpType, Instruction icmpInstruction1, Instruction icmpInstruction2) {
        super(function == null ? null : function.getCurBasicBlock(), function == null ? null : function.getNextInstructionName(),
                ValueType.INTEGER, SymbolType.Bool, new ArrayList<>(Arrays.asList(icmpInstruction1, icmpInstruction2)));
        this.cmpType = cmpType;
        if (icmpInstruction1.getValue() != null && icmpInstruction2.getValue() != null) {
            switch (cmpType.getIcmpInstruction()) {
                case "slt":
                    this.setValue(icmpInstruction1.getValue() < icmpInstruction2.getValue() ? 1 : 0);
                    break;
                case "sle":
                    this.setValue(icmpInstruction1.getValue() <= icmpInstruction2.getValue() ? 1 : 0);
                    break;
                case "sgt":
                    this.setValue(icmpInstruction1.getValue() > icmpInstruction2.getValue() ? 1 : 0);
                    break;
                case "sge":
                    this.setValue(icmpInstruction1.getValue() >= icmpInstruction2.getValue() ? 1 : 0);
                    break;
                case "eq":
                    this.setValue(icmpInstruction1.getValue().equals(icmpInstruction2.getValue()) ? 1 : 0);
                    break;
                case "ne":
                    this.setValue(!icmpInstruction1.getValue().equals(icmpInstruction2.getValue()) ? 1 : 0);
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return "\t%" + getName() + " = icmp " + cmpType.getIcmpInstruction() + " " + getUses().get(0).toTypeAndNameString()
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
            StringBuilder sb = new StringBuilder();
            if (uses.get(0).getValue() != null) {
                sb.append("\tli $t0, ").append(uses.get(0).getValue()).append("\n");
            } else {
                sb.append("\tlw $t0, ").append(((Instruction) uses.get(0)).getSpOffset()).append("($sp)\n");
            }
            if (uses.get(1).getValue() != null) {
                sb.append("\tli $t1, ").append(uses.get(1).getValue()).append("\n");
            } else {
                sb.append("\tlw $t1, ").append(((Instruction) uses.get(1)).getSpOffset()).append("($sp)\n");
            }
            switch (cmpType.getIcmpInstruction()) {
                case "slt":
                    sb.append("\tslt $t0, $t0, $t1\n")
                            .append("\tsw $t0, ").append(getSpOffset()).append("($sp)\n");
                    break;
                case "sle":
                    sb.append("\tslt $t0, $t0, $t1\n")
                            .append("\txori $t0, $t0, 1\n")
                            .append("\tsw $t0, ").append(getSpOffset()).append("($sp)\n");
                    break;
                case "sgt":
                    sb.append("\tslt $t0, $t1, $t0\n")
                            .append("\tsw $t0, ").append(getSpOffset()).append("($sp)\n");
                    break;
                case "sge":
                    sb.append("\tslt $t0, $t1, $t0\n")
                            .append("\txori $t0, $t0, 1\n")
                            .append("\tsw $t0, ").append(getSpOffset()).append("($sp)\n");
                    break;
                case "eq":
                    sb.append("\tslt $t2, $t0, $t1\n")
                            .append("\tslt $t1, $t1, $t0\n")
                            .append("\tor $t0, $t1, $t2\n")
                            .append("\txori $t0, $t0, 1\n")
                            .append("\tsw $t0, ").append(getSpOffset()).append("($sp)\n");
                    break;
                case "ne":
                    sb.append("\tslt $t0, $t0, $t1\n")
                            .append("\tslt $t1, $t1, $t0\n")
                            .append("\tor $t0, $t1, $t2\n")
                            .append("\tsw $t0, ").append(getSpOffset()).append("($sp)\n");
                    break;
            }
            return sb.toString();
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
