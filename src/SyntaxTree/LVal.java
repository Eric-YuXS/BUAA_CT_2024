package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.*;
import frontend.*;
import frontend.Error;

public class LVal implements SyntaxTreeNode {  // LVal → Ident ['[' Exp ']']
    private final Token ident;
    private final Token lBrack;
    private final Exp exp;
    private final Token rBrack;

    public LVal(Token ident, Token lBrack, Exp exp, Token rBrack) {
        this.ident = ident;
        this.lBrack = lBrack;
        this.exp = exp;
        this.rBrack = rBrack;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident);
        if (lBrack != null) {
            sb.append(lBrack).append(exp);
            if (rBrack != null) {
                sb.append(rBrack);
            }
        }
        return sb.append("<LVal>\n").toString();
    }

    public SymbolType errorAnalyze(SymbolStack symbolStack, boolean isAssign) {
        Symbol symbol = symbolStack.getSymbol(ident.getString());
        if (symbol == null) {
            symbolStack.addError(new Error(ident.getLineNumber(), 'c'));
//        } else if (exp != null && (symbol.getSymbolType().isVar() || symbol.getSymbolType().isFunc())) {
//            System.err.println(ident.getString() + "is not an array!");
//            return null;
//        } else if (exp == null && (symbol.getSymbolType().isArray() || symbol.getSymbolType().isFunc())) {
//            System.err.println(ident.getString() + "is not a variable!");
//            return null;
        } else if (symbol.getSymbolType().isConst() && isAssign) {
            symbolStack.addError(new Error(ident.getLineNumber(), 'h'));
        }
        if (exp != null) {
            exp.errorAnalyze(symbolStack);
            return symbol != null ? symbol.getSymbolType().arrayOrVarToVar() : null;
        } else {
            return symbol != null ? symbol.getSymbolType() : null;
        }
    }

    public Instruction analyze(SymbolStack symbolStack, Function function, boolean isAssign) {
        Symbol symbol = symbolStack.getSymbol(ident.getString());
        if (symbol == null) {
            symbolStack.addError(new Error(ident.getLineNumber(), 'c'));
//        } else if (exp != null && (symbol.getSymbolType().isVar() || symbol.getSymbolType().isFunc())) {
//            System.err.println(ident.getString() + "is not an array!");
//            return null;
//        } else if (exp == null && (symbol.getSymbolType().isArray() || symbol.getSymbolType().isFunc())) {
//            System.err.println(ident.getString() + "is not a variable!");
//            return null;
        } else if (symbol.getSymbolType().isConst() && isAssign) {
            symbolStack.addError(new Error(ident.getLineNumber(), 'h'));
        }
        if (exp != null) {
            Instruction indexInstruction = exp.analyze(symbolStack, function);
            if (function == null) {
                if (symbol.getSymbolType().isI32()) {
                    if (symbol.getInstruction() instanceof AllocaGlobal) {
                        return Num.getNum(null, ((AllocaGlobal) symbol.getInstruction()).getInitValue(indexInstruction.getValue()));
                    } else {
                        return Num.getNum(null, ((AllocaGlobalString) symbol.getInstruction()).getInitValue(indexInstruction.getValue()));
                    }
                } else {
                    if (symbol.getInstruction() instanceof AllocaGlobal) {
                        return Num.getChar(null, ((AllocaGlobal) symbol.getInstruction()).getInitValue(indexInstruction.getValue()));
                    } else {
                        return Num.getChar(null, ((AllocaGlobalString) symbol.getInstruction()).getInitValue(indexInstruction.getValue()));
                    }
                }
            } else {
                Instruction getElementPointerInstruction = new GetElementPointer(function,
                        symbol.getSymbolType(), symbol.getInstruction(), indexInstruction,
                        symbol.getInstruction() instanceof Alloca ? ((Alloca) symbol.getInstruction()).getSize() :
                                symbol.getInstruction() instanceof AllocaGlobal ? ((AllocaGlobal) symbol.getInstruction()).getSize() :
                                        symbol.getInstruction() instanceof AllocaGlobalString ?
                                                ((AllocaGlobalString) symbol.getInstruction()).getSize() : -1);
                function.getCurBasicBlock().addInstruction(getElementPointerInstruction);
                return getElementPointerInstruction;
            }
        } else if (symbol.getSymbolType().isArray()) {
            Instruction getElementPointerInstruction = new GetArrayPointer(function, symbol.getSymbolType(), symbol.getInstruction(),
                    symbol.getInstruction() instanceof Alloca ? ((Alloca) symbol.getInstruction()).getSize() :
                            symbol.getInstruction() instanceof AllocaGlobal ? ((AllocaGlobal) symbol.getInstruction()).getSize() :
                                    symbol.getInstruction() instanceof AllocaGlobalString ?
                                            ((AllocaGlobalString) symbol.getInstruction()).getSize() : -1);
            function.getCurBasicBlock().addInstruction(getElementPointerInstruction);
            return getElementPointerInstruction;
        } else {
            if (function == null) {
                if (symbol.getSymbolType().isI32()) {
                    return Num.getNum(null, ((VarGlobal) symbol.getInstruction()).getInitValue());
                } else {
                    return Num.getChar(null, ((VarGlobal) symbol.getInstruction()).getInitValue());
                }
            } else {
                return symbol.getInstruction();
            }
        }
    }
}
