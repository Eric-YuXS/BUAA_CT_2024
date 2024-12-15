package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.Call;
import LLVMIR.Instructions.Trunc;
import LLVMIR.Instructions.Zext;
import frontend.*;
import frontend.Error;

import java.util.ArrayList;

public class UnaryExp2 extends UnaryExp {  // UnaryExp → Ident '(' [FuncRParams] ')'
    private final Token ident;
    private final Token lParent;
    private final FuncRParams funcRParams;
    private final Token rParent;

    public UnaryExp2(Token ident, Token lParent, FuncRParams funcRParams, Token rParent) {
        super();
        this.ident = ident;
        this.lParent = lParent;
        this.funcRParams = funcRParams;
        this.rParent = rParent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident).append(lParent);
        if (funcRParams != null) {
            sb.append(funcRParams);
        }
        if (rParent != null) {
            sb.append(rParent);
        }
        return sb.append("<UnaryExp>\n").toString();
    }

    @Override
    public Instruction analyze(SymbolStack symbolStack, Function function) {
        Symbol symbol = symbolStack.getSymbol(ident.getString());
        if (symbol == null) {
            symbolStack.addError(new Error(ident.getLineNumber(), 'c'));
            if (funcRParams != null) {
                funcRParams.analyze(symbolStack, function);
            }
            return null;
        } else if (symbol instanceof FuncSymbol) {
            ArrayList<Symbol> fParams = ((FuncSymbol) symbol).getFParams();
            int fParamsIndex = 0;
            boolean typeErrorFlag = false;
            boolean numErrorFlag = false;
            ArrayList<Instruction> instructions;
            if (funcRParams != null) {
                instructions = funcRParams.analyze(symbolStack, function);
                for (int i = 0; i < instructions.size(); i++) {
                    if (fParamsIndex < fParams.size()) {
                        if (!fParams.get(fParamsIndex).getSymbolType().canAcceptRParam(instructions.get(i).getSymbolType())) {
                            typeErrorFlag = true;
                            fParamsIndex++;
                        } else if (fParams.get(fParamsIndex).getSymbolType().isI8() && instructions.get(i).getSymbolType().isI32()) {
                            Instruction newInstruction = new Trunc(function, instructions.get(i));
                            function.getCurBasicBlock().addInstruction(newInstruction);
                            instructions.set(i, newInstruction);
                            fParamsIndex++;
                        } else if (fParams.get(fParamsIndex).getSymbolType().isI32() && instructions.get(i).getSymbolType().isI8()) {
                            Instruction newInstruction = new Zext(function, instructions.get(i));
                            function.getCurBasicBlock().addInstruction(newInstruction);
                            instructions.set(i, newInstruction);
                            fParamsIndex++;
                        } else {
                            fParamsIndex++;
                        }
                    } else {
                        numErrorFlag = true;
                    }
                }
            } else {
                instructions = new ArrayList<>();
            }
            if (numErrorFlag || fParamsIndex < fParams.size()) {
                symbolStack.addError(new Error(ident.getLineNumber(), 'd'));
            }
            if (typeErrorFlag) {
                symbolStack.addError(new Error(ident.getLineNumber(), 'e'));
            }
            Call callInstruction = new Call(function, ((FuncSymbol) symbol).getFunction(), instructions);
            function.getCurBasicBlock().addInstruction(callInstruction);
            return callInstruction;
        } else {
            System.err.println(ident.getString() + "is not a function!");
            return null;
        }
    }
}
