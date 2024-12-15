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
}
