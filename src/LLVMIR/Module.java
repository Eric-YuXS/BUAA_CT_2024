package LLVMIR;

import java.util.ArrayList;
import java.util.HashMap;

public class Module {
    private final ArrayList<Instruction> instructions;
    private final HashMap<String, Instruction> stringInstructions;
    private int stringInstructionCount = 0;
    private final ArrayList<Function> functions;

    public Module() {
        instructions = new ArrayList<>();
        stringInstructions = new HashMap<>();
        functions = new ArrayList<>();
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public void addStringInstruction(String string, Instruction instruction) {
        stringInstructions.put(string, instruction);
    }

    public boolean containsStringInstruction(String string) {
        return stringInstructions.containsKey(string);
    }

    public Instruction getStringInstruction(String string) {
        return stringInstructions.get(string);
    }

    public void addFunction(Function function) {
        functions.add(function);
    }

    public Function getCurFunction() {
        if (functions.isEmpty()) {
            return null;
        } else {
            return functions.get(functions.size() - 1);
        }
    }

    public String getNextStringInstructionName() {
        if (stringInstructionCount == 0) {
            stringInstructionCount++;
            return ".str";
        } else {
            return ".str." + stringInstructionCount++;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("declare i32 @getint()\n" +
                "declare i32 @getchar()\n" +
                "declare void @putint(i32)\n" +
                "declare void @putch(i32)\n" +
                "declare void @putstr(i8*)\n");
        for (Instruction instruction : instructions) {
            sb.append(instruction);
        }
        for (Instruction instruction : stringInstructions.values()) {
            sb.append(instruction);
        }
        for (Function function : functions) {
            sb.append(function);
        }
        return sb.toString();
    }

    public String toMips() {
        StringBuilder sb = new StringBuilder();
        sb.append(".data\n");
        for (Instruction instruction : instructions) {
            sb.append(instruction.toMips());
        }
        for (Instruction instruction : stringInstructions.values()) {
            sb.append(instruction.toMips());
        }
        sb.append(".text\n\tjal main\n\tli $v0, 10\n\tsyscall\n");
        for (Function function : functions) {
            sb.append("\n").append(function.toMips());
        }
        return sb.toString();
    }
}
