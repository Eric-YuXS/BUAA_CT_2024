package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.*;
import LLVMIR.Module;
import frontend.Error;
import frontend.Symbol;
import frontend.SymbolStack;
import frontend.SymbolType;
import frontend.Token;

import java.util.ArrayList;

public class ConstDef implements SyntaxTreeNode {  // ConstDef → Ident [ '[' ConstExp ']' ] '=' ConstInitVal
    private final Token ident;
    private final Token lBrack;
    private final ConstExp constExp;
    private final Token rBrack;
    private final Token assign;
    private final ConstInitVal constInitVal;

    public ConstDef(Token ident, Token lBrack, ConstExp constExp, Token rBrack, Token assign, ConstInitVal constInitVal) {
        this.ident = ident;
        this.lBrack = lBrack;
        this.constExp = constExp;
        this.rBrack = rBrack;
        this.assign = assign;
        this.constInitVal = constInitVal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident);
        if (lBrack != null) {
            sb.append(lBrack).append(constExp);
            if (rBrack != null) {
                sb.append(rBrack);
            }
        }
        return sb.append(assign).append(constInitVal).append("<ConstDef>\n").toString();
    }

    public void analyze(SymbolStack symbolStack, Module module, SymbolType symbolType) {
        Function function = module.getCurFunction();
        if (constExp != null) {
            symbolType = symbolType.constToConstArray();
            Instruction constInstruction = constExp.analyze(symbolStack, function);
            if (constInstruction.getValue() == null) {
                System.err.println("Size of the array is uncertain!");
            }
            Symbol symbol = new Symbol(symbolType, ident.getString());
            if (!symbolStack.addSymbol(symbol)) {
                symbolStack.addError(new Error(ident.getLineNumber(), 'b'));
            }
            if (function != null) {
                Alloca allocaInstruction = new Alloca(function, symbolType, constInstruction.getValue());
                symbol.setInstruction(allocaInstruction);
                function.getCurBasicBlock().addInstruction(allocaInstruction);
                ArrayList<Instruction> valueInstructions = constInitVal.analyze(symbolStack, function);
                if (constInitVal instanceof ConstInitVal2) {
                    for (int i = 0; i < valueInstructions.size(); i++) {
                        Instruction valueInstruction = valueInstructions.get(i);
                        if (valueInstruction.getSymbolType().isI32() && symbol.getSymbolType().arrayOrVarToVar().isI8()) {
                            valueInstruction = new Trunc(function, valueInstruction);
                            function.getCurBasicBlock().addInstruction(valueInstruction);
                        } else if (valueInstruction.getSymbolType().isI8() && symbol.getSymbolType().arrayOrVarToVar().isI32()) {
                            valueInstruction = new Zext(function, valueInstruction);
                            function.getCurBasicBlock().addInstruction(valueInstruction);
                        }
                        Instruction getElementPointerInstruction = new GetElementPointer(function,
                                symbol.getSymbolType(), symbol.getInstruction(), Num.getNum(function, i),
                                ((Alloca) symbol.getInstruction()).getSize());
                        function.getCurBasicBlock().addInstruction(getElementPointerInstruction);
                        Instruction storeInstruction = new Store(function, getElementPointerInstruction, valueInstruction);
                        function.getCurBasicBlock().addInstruction(storeInstruction);
                    }
                } else if (constInitVal instanceof ConstInitVal3) {
                    String string = ((ConstInitVal3) constInitVal).getString();
                    for (int i = 0; i < string.length(); i++) {
                        Instruction getElementPointerInstruction = new GetElementPointer(function,
                                symbol.getSymbolType(), symbol.getInstruction(), Num.getNum(function, i),
                                ((Alloca) symbol.getInstruction()).getSize());
                        function.getCurBasicBlock().addInstruction(getElementPointerInstruction);
                        Instruction storeInstruction = new Store(function, getElementPointerInstruction,
                                Num.getChar(function, string.charAt(i)));
                        function.getCurBasicBlock().addInstruction(storeInstruction);
                    }
                } else {
                    System.err.println("Cannot assign a variable to an array!");
                }
            } else {
                if (constInitVal instanceof ConstInitVal2) {
                    AllocaGlobal allocaGlobalInstruction = new AllocaGlobal(symbol, constInstruction.getValue(),
                            constInitVal.analyze(symbolStack, null));
                    symbol.setInstruction(allocaGlobalInstruction);
                    module.addInstruction(allocaGlobalInstruction);
                } else if (constInitVal instanceof ConstInitVal3) {
                    AllocaGlobalString allocaGlobalStringInstruction = new AllocaGlobalString(symbol,
                            constInstruction.getValue(), ((ConstInitVal3) constInitVal).getString());
                    symbol.setInstruction(allocaGlobalStringInstruction);
                    module.addInstruction(allocaGlobalStringInstruction);
                } else {
                    System.err.println("Cannot assign a variable to an array!");
                }
            }
        } else {
            Symbol symbol = new Symbol(symbolType, ident.getString());
            if (!symbolStack.addSymbol(symbol)) {
                symbolStack.addError(new Error(ident.getLineNumber(), 'b'));
            }
            if (function != null) {
                Var varInstruction = new Var(function, symbolType);
                symbol.setInstruction(varInstruction);
                function.getCurBasicBlock().addInstruction(varInstruction);
                ArrayList<Instruction> valueInstructions = constInitVal.analyze(symbolStack, function);
                if (constInitVal instanceof ConstInitVal1) {
                    Instruction valueInstruction = valueInstructions.get(0);
                    if (varInstruction.getSymbolType().isI8()) {
                        valueInstruction = new Trunc(function, valueInstruction);
                        function.getCurBasicBlock().addInstruction(valueInstruction);
                    }
                    function.getCurBasicBlock().addInstruction(new Store(function, varInstruction, valueInstruction));
                } else {
                    System.err.println("Cannot assign an array to a variable!");
                }
            } else {
                if (constInitVal instanceof ConstInitVal1) {
                    VarGlobal varGlobalInstruction = new VarGlobal(symbol, constInitVal.analyze(symbolStack, null).get(0));
                    symbol.setInstruction(varGlobalInstruction);
                    module.addInstruction(varGlobalInstruction);
                } else {
                    System.err.println("Cannot assign an array to a variable!");
                }
            }
        }
    }
}
