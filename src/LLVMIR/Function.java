package LLVMIR;

import frontend.FuncSymbol;
import frontend.SymbolType;

import java.util.ArrayList;

public class Function extends Value {
    private final Module module;
    private final FuncSymbol funcSymbol;
    private final ArrayList<Instruction> paramInstructions;
    private final ArrayList<BasicBlock> basicBlocks;
    private int instructionCount;
    private int basicBlockCount;

    public Function(Module module, FuncSymbol funcSymbol) {
        super(funcSymbol.getSymbolName(), funcSymbol.getSymbolType() == SymbolType.VoidFunc ? ValueType.VOID : ValueType.INTEGER,
                funcSymbol.getSymbolType(), new ArrayList<>());
        this.module = module;
        this.funcSymbol = funcSymbol;
        this.paramInstructions = new ArrayList<>();
        this.basicBlocks = new ArrayList<>();
        instructionCount = 1;
        basicBlockCount = 1;
        basicBlocks.add(new BasicBlock(this));
    }

    public void addParamInstruction(Instruction instruction) {
        paramInstructions.add(instruction);
    }

    public BasicBlock getCurBasicBlock() {
        return basicBlocks.get(basicBlocks.size() - 1);
    }

    public void enterNextBasicBlock() {
        basicBlocks.add(new BasicBlock(this));
    }

    public void enterNextBasicBlock(BasicBlock nextBasicBlock) {
        nextBasicBlock.setFunction(this);
        basicBlocks.add(nextBasicBlock);
    }

    public FuncSymbol getFuncSymbol() {
        return funcSymbol;
    }

    public Module getModule() {
        return module;
    }

    public String getNextInstructionName() {
        return "v" + instructionCount++;
    }

    public String getNextBasicBlockName() {
        return "b" + basicBlockCount++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("define dso_local ").append(funcSymbol.toValueString()).append("(");
        if (!paramInstructions.isEmpty()) {
            sb.append(paramInstructions.get(0).toTypeAndNameString());
            for (int i = 1; i < paramInstructions.size(); i++) {
                sb.append(", ").append(paramInstructions.get(i).toTypeAndNameString());
            }
        }
        sb.append(") {\n");
        for (BasicBlock basicBlock : basicBlocks) {
            sb.append(basicBlock);
        }
        return sb.append("}\n").toString();
    }

    @Override
    public String toNameString() {
        return "@" + getName();
    }

    @Override
    public String toTypeAndNameString() {
        return getSymbolType().toValueString() + " @" + getName();
    }
}
