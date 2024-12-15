package LLVMIR.Instructions;

import LLVMIR.Function;
import LLVMIR.Instruction;
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
}
