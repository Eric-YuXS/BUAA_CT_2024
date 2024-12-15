package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
import LLVMIR.Instructions.BrUnconditional;
import frontend.Error;
import frontend.SymbolStack;
import frontend.Token;

public class Stmt7 extends Stmt {  // Stmt → 'continue' ';'
    private final Token continueTk;
    private final Token semicn;

    public Stmt7(Token continueTk, Token semicn) {
        this.continueTk = continueTk;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(continueTk);
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<Stmt>\n").toString();
    }

    @Override
    public void analyze(SymbolStack symbolStack, Function function, BasicBlock forCondBasicBlock, BasicBlock forEndBasicBlock) {
        if (forCondBasicBlock == null || forEndBasicBlock == null) {
            symbolStack.addError(new Error(continueTk.getLineNumber(), 'm'));
        } else {
            function.getCurBasicBlock().addInstruction(new BrUnconditional(function.getCurBasicBlock(), forCondBasicBlock));
            function.enterNextBasicBlock();
        }
    }
}
