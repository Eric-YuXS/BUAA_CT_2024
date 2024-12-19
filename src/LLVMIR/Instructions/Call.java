package LLVMIR.Instructions;

import LLVMIR.*;

import java.util.ArrayList;
import java.util.Collections;

public class Call extends Instruction {
    public Call(Function function, Function calledFunction, ArrayList<Instruction> paramInstructions) {
        super(function.getCurBasicBlock(), calledFunction.getValueType() == ValueType.VOID ? null : function.getNextInstructionName(),
                calledFunction.getValueType() == ValueType.VOID ? ValueType.VOID : ValueType.INTEGER,
                calledFunction.getSymbolType(), new ArrayList<>(Collections.singletonList(calledFunction)));
        this.addInstructionUses(paramInstructions);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t");
        if (getValueType() != ValueType.VOID) {
            sb.append("%").append(getName()).append(" = ");
        }
        ArrayList<Value> uses = getUses();
        sb.append("call ").append(uses.get(0).toTypeAndNameString()).append("(");
        if (uses.size() > 1) {
            sb.append(uses.get(1).toTypeAndNameString());
            for (int i = 2; i < uses.size(); i++) {
                sb.append(", ").append(uses.get(i).toTypeAndNameString());
            }
        }
        return sb.append(")\n").toString();
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
        ArrayList<Value> uses = getUses();
        StringBuilder sb = new StringBuilder();
        if (uses.size() > 5) {
            for (int i = 1; i < 5; i++) {
                if (uses.get(i).getValue() != null) {
                    sb.append("\tli $a").append(i - 1).append(", ").append(uses.get(i).getValue()).append("\n");
                } else if (uses.get(i).getSymbolType().isArray()) {
                    sb.append("\taddiu $a").append(i - 1).append(", $sp, ").append(((Instruction) uses.get(i)).getSpOffset()).append("\n");
                } else {
                    sb.append("\tlw $a").append(i - 1).append(", ").append(((Instruction) uses.get(i)).getSpOffset()).append("($sp)\n");
                }
            }
            sb.append("\taddiu $sp, $sp, -").append((uses.size() - 5) * 4).append("\n");
            for (int i = 5; i < uses.size(); i++) {
                if (uses.get(i).getValue() != null) {
                    sb.append("\tli $t0, ").append(uses.get(i).getValue()).append("\n")
                            .append("\tsw $t0, ").append(i - 5).append("($sp)\n");
                } else if (uses.get(i).getSymbolType().isArray()) {
                    sb.append("\taddiu $a").append(i - 1).append(", $sp, ").append(((Instruction) uses.get(i)).getSpOffset()).append("\n");
                } else {
                    sb.append("\tlw $t0, ").append(((Instruction) uses.get(i)).getSpOffset()).append("($sp)\n")
                            .append("\tsw $t0, ").append(i - 5).append("($sp)\n");
                }
            }
            sb.append("\tjal ").append(uses.get(0).getName()).append("\n");
            sb.append("\taddiu $sp, $sp, ").append((uses.size() - 5) * 4).append("\n");
        } else {
            for (int i = 1; i < uses.size(); i++) {
                if (uses.get(i).getValue() != null) {
                    sb.append("\tli $a").append(i - 1).append(", ").append(uses.get(i).getValue()).append("\n");
                } else if (uses.get(i).getSymbolType().isArray()) {
                    sb.append("\taddiu $a").append(i - 1).append(", $sp, ").append(((Instruction) uses.get(i)).getSpOffset()).append("\n");
                } else {
                    sb.append("\tlw $a").append(i - 1).append(", ").append(((Instruction) uses.get(i)).getSpOffset()).append("($sp)\n");
                }
            }
            sb.append("\tjal ").append(uses.get(0).getName()).append("\n");
        }
        sb.append("\tsw $v0, ").append(getSpOffset()).append("($sp)\n");
        return sb.toString();
    }

    @Override
    public int countMemUse(int count) {
        setSpOffset(count);
        return count + 4;
    }
}
