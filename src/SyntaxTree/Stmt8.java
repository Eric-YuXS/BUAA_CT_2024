package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.Ret;
import LLVMIR.Instructions.Trunc;
import frontend.*;
import frontend.Error;

public class Stmt8 extends Stmt {  // Stmt → 'return' [Exp] ';'
    private final Token returnTk;
    private final Exp exp;
    private final Token semicn;

    public Stmt8(Token returnTk, Exp exp, Token semicn) {
        super();
        this.returnTk = returnTk;
        this.exp = exp;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(returnTk);
        if (exp != null) {
            sb.append(exp);
        }
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<Stmt>\n").toString();
    }

    @Override
    public void errorAnalyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean isLoop) {
        if (funcSymbol.getSymbolType() == SymbolType.VoidFunc && exp != null) {
            symbolStack.addError(new Error(returnTk.getLineNumber(), 'f'));
//        } else if (funcSymbol.getSymbolType() != SymbolType.VoidFunc && exp == null) {
//            System.err.println("Return without exp in " + funcSymbol);
        }
        if (exp != null) {
            exp.errorAnalyze(symbolStack);
        }
    }

    @Override
    public void analyze(SymbolStack symbolStack, Function function, BasicBlock forCondBasicBlock, BasicBlock forEndBasicBlock) {
        if (function.getFuncSymbol().getSymbolType() == SymbolType.VoidFunc && exp != null) {
            symbolStack.addError(new Error(returnTk.getLineNumber(), 'f'));
//        } else if (funcSymbol.getSymbolType() != SymbolType.VoidFunc && exp == null) {
//            System.err.println("Return without exp in " + funcSymbol);
        }
        if (exp != null) {
            Instruction expInstruction = exp.analyze(symbolStack, function);
            if (function.getFuncSymbol().getSymbolType() == SymbolType.CharFunc) {
                expInstruction = new Trunc(function, expInstruction);
                function.getCurBasicBlock().addInstruction(expInstruction);
            }
            function.getCurBasicBlock().addInstruction(new Ret(function, expInstruction));
        } else {
            function.getCurBasicBlock().addInstruction(new Ret(function, null));
        }
    }
}
