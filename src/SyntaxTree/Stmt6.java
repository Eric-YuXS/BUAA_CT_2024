package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
import LLVMIR.Instructions.BrUnconditional;
import frontend.Error;
import frontend.SymbolStack;
import frontend.Token;

public class Stmt6 extends Stmt {  // Stmt → 'break' ';'
    private final Token breakTk;
    private final Token semicn;

    public Stmt6(Token breakTk, Token semicn) {
        this.breakTk = breakTk;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(breakTk);
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<Stmt>\n").toString();
    }

    @Override
    public void analyze(SymbolStack symbolStack, Function function, BasicBlock forCondBasicBlock, BasicBlock forEndBasicBlock) {
        if (forCondBasicBlock == null || forEndBasicBlock == null) {
            symbolStack.addError(new Error(breakTk.getLineNumber(), 'm'));
        } else {
            function.getCurBasicBlock().addInstruction(new BrUnconditional(function.getCurBasicBlock(), forEndBasicBlock));
            function.enterNextBasicBlock();
        }
    }
}
