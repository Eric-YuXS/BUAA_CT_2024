package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.*;
import LLVMIR.Module;
import frontend.*;
import frontend.Error;

import java.util.ArrayList;

public class VarDef implements SyntaxTreeNode {  // VarDef → Ident [ '[' ConstExp ']' ] | Ident [ '[' ConstExp ']' ] '=' InitVal
    private final Token ident;
    private final Token lBrack;
    private final ConstExp constExp;
    private final Token rBrack;
    private final Token assign;
    private final InitVal initVal;

    public VarDef(Token ident, Token lBrack, ConstExp constExp, Token rBrack, Token assign, InitVal initVal) {
        this.ident = ident;
        this.lBrack = lBrack;
        this.constExp = constExp;
        this.rBrack = rBrack;
        this.assign = assign;
        this.initVal = initVal;
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
        if (assign != null) {
            sb.append(assign).append(initVal);
        }
        return sb.append("<VarDef>\n").toString();
    }

    public void errorAnalyze(SymbolStack symbolStack, SymbolType symbolType) {
        if (constExp != null) {
            symbolType = symbolType.varToArray();
            constExp.errorAnalyze(symbolStack);
        }
        if (!symbolStack.addSymbol(new Symbol(symbolType, ident.getString()))) {
            symbolStack.addError(new Error(ident.getLineNumber(), 'b'));
        }
        if (initVal != null) {
            initVal.errorAnalyze(symbolStack);
        }
    }

    public void analyze(SymbolStack symbolStack, Module module, SymbolType symbolType) {
        Function function = module.getCurFunction();
        if (constExp != null) {
            symbolType = symbolType.varToArray();
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
                if (initVal != null) {
                    ArrayList<Instruction> valueInstructions = initVal.analyze(symbolStack, function);
                    if (initVal instanceof InitVal2) {
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
                    } else if (initVal instanceof InitVal3) {
                        ArrayList<Integer> charNums = prepareStringChars(((InitVal3) initVal).getString());
                        for (int i = 0; i < charNums.size(); i++) {
                            Instruction getElementPointerInstruction = new GetElementPointer(function,
                                    symbol.getSymbolType(), symbol.getInstruction(), Num.getNum(function, i),
                                    ((Alloca) symbol.getInstruction()).getSize());
                            function.getCurBasicBlock().addInstruction(getElementPointerInstruction);
                            Instruction storeInstruction = new Store(function, getElementPointerInstruction,
                                    Num.getChar(function, charNums.get(i)));
                            function.getCurBasicBlock().addInstruction(storeInstruction);
                        }
                    } else {
                        System.err.println("Cannot assign a variable to an array!");
                    }
                }
            } else {
                if (initVal != null) {
                    if (initVal instanceof InitVal2) {
                        AllocaGlobal allocaGlobalInstruction = new AllocaGlobal(symbol, constInstruction.getValue(),
                                initVal.analyze(symbolStack, null));
                        symbol.setInstruction(allocaGlobalInstruction);
                        module.addInstruction(allocaGlobalInstruction);
                    } else if (initVal instanceof InitVal3) {
                        AllocaGlobalString allocaGlobalStringInstruction = new AllocaGlobalString(symbol,
                                constInstruction.getValue(), ((InitVal3) initVal).getString());
                        symbol.setInstruction(allocaGlobalStringInstruction);
                        module.addInstruction(allocaGlobalStringInstruction);
                    } else {
                        System.err.println("Cannot assign a variable to an array!");
                    }
                } else {
                    AllocaGlobal allocaGlobalInstruction = new AllocaGlobal(symbol, constInstruction.getValue(), new ArrayList<>());
                    symbol.setInstruction(allocaGlobalInstruction);
                    module.addInstruction(allocaGlobalInstruction);
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
                if (initVal != null) {
                    ArrayList<Instruction> valueInstructions = initVal.analyze(symbolStack, function);
                    if (initVal instanceof InitVal1) {
                        Instruction valueInstruction = valueInstructions.get(0);
                        if (varInstruction.getSymbolType().isI8()) {
                            valueInstruction = new Trunc(function, valueInstruction);
                            function.getCurBasicBlock().addInstruction(valueInstruction);
                        }
                        function.getCurBasicBlock().addInstruction(new Store(function, varInstruction, valueInstruction));
                    } else {
                        System.err.println("Cannot assign an array to a variable!");
                    }
                }
            } else {
                if (initVal != null) {
                    if (initVal instanceof InitVal1) {
                        VarGlobal varGlobalInstruction = new VarGlobal(symbol, initVal.analyze(symbolStack, null).get(0));
                        symbol.setInstruction(varGlobalInstruction);
                        module.addInstruction(varGlobalInstruction);
                    } else {
                        System.err.println("Cannot assign an array to a variable!");
                    }
                } else {
                    VarGlobal varGlobalInstruction = new VarGlobal(symbol, null);
                    symbol.setInstruction(varGlobalInstruction);
                    module.addInstruction(varGlobalInstruction);
                }
            }
        }
    }

    private ArrayList<Integer> prepareStringChars(String string) {
        string = string.substring(1, string.length() - 1);
        ArrayList<Integer> charNums = new ArrayList<>();
        char[] chars = string.toCharArray();
        int i = 0;
        while (i < chars.length) {
            char c = chars[i];
            if (c == '\\' && i + 1 < chars.length) {
                char next = chars[i + 1];
                switch (next) {
                    case 'a':
                        charNums.add(7);
                        break;
                    case 'b':
                        charNums.add(8);
                        break;
                    case 't':
                        charNums.add(9);
                        break;
                    case 'n':
                        charNums.add(10);
                        break;
                    case 'v':
                        charNums.add(11);
                        break;
                    case 'f':
                        charNums.add(12);
                        break;
                    case 'r':
                        charNums.add(13);
                        break;
                    case '0':
                        charNums.add(0);
                        break;
                    case '\\':
                        charNums.add(92);
                        break;
                    case '"':
                        charNums.add(34);
                        break;
                    case '\'':
                        charNums.add(39);
                        break;
                    default:
                        charNums.add((int) next);
                        break;
                }
                i += 2;
            } else {
                charNums.add((int) c);
                i++;
            }
        }
        charNums.add(0);
        return charNums;
    }
}
