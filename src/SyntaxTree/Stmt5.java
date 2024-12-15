package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
import LLVMIR.Instructions.BrConditional;
import LLVMIR.Instructions.BrUnconditional;
import frontend.SymbolStack;
import frontend.Token;

import java.util.ArrayList;

public class Stmt5 extends Stmt {  // Stmt → 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
    private final Token forTk;
    private final Token lParent;
    private final ForStmt forStmt1;
    private final Token semicn1;
    private final Cond cond;
    private final Token semicn2;
    private final ForStmt forStmt2;
    private final Token rParent;
    private final Stmt stmt;

    public Stmt5(Token forTk, Token lParent, ForStmt forStmt1, Token semicn1, Cond cond, Token semicn2,
                 ForStmt forStmt2, Token rParent, Stmt stmt) {
        super();
        this.forTk = forTk;
        this.lParent = lParent;
        this.forStmt1 = forStmt1;
        this.semicn1 = semicn1;
        this.cond = cond;
        this.semicn2 = semicn2;
        this.forStmt2 = forStmt2;
        this.rParent = rParent;
        this.stmt = stmt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(forTk).append(lParent);
        if (forStmt1 != null) {
            sb.append(forStmt1);
        }
        sb.append(semicn1);
        if (cond != null) {
            sb.append(cond);
        }
        sb.append(semicn2);
        if (forStmt2 != null) {
            sb.append(forStmt2);
        }
        if (rParent != null) {
            sb.append(rParent);
        }
        return sb.append(stmt).append("<Stmt>\n").toString();
    }

    @Override
    public void analyze(SymbolStack symbolStack, Function function, BasicBlock forCondBasicBlock, BasicBlock forEndBasicBlock) {
        if (forStmt1 != null) {
            forStmt1.analyze(symbolStack, function);
        }
        BrUnconditional brInstruction = new BrUnconditional(function.getCurBasicBlock(), null);
        function.getCurBasicBlock().addInstruction(brInstruction);
        function.enterNextBasicBlock();
        brInstruction.setBrBasicBlock(function.getCurBasicBlock());
        if (cond != null) {
            ArrayList<BrConditional> condBrInstructions = cond.analyze(symbolStack, function);
            BasicBlock condBasicBlock = function.getCurBasicBlock();
            function.enterNextBasicBlock();
            BasicBlock forBasicBlock = function.getCurBasicBlock();
            BasicBlock endForBasicBlock = new BasicBlock(null);
            BasicBlock afterForBasicBlock = new BasicBlock(null);
            stmt.analyze(symbolStack, function, endForBasicBlock, afterForBasicBlock);

            brInstruction = new BrUnconditional(function.getCurBasicBlock(), null);
            function.getCurBasicBlock().addInstruction(brInstruction);
            function.enterNextBasicBlock(endForBasicBlock);
            brInstruction.setBrBasicBlock(function.getCurBasicBlock());
            if (forStmt2 != null) {
                forStmt2.analyze(symbolStack, function);
            }
            function.getCurBasicBlock().addInstruction(new BrUnconditional(function.getCurBasicBlock(), condBasicBlock));

            function.enterNextBasicBlock(afterForBasicBlock);
            for (BrConditional condBrInstruction : condBrInstructions) {
                condBrInstruction.setBrBasicBlock1(forBasicBlock);
                condBrInstruction.setBrBasicBlock2(function.getCurBasicBlock());
            }
        } else {
            BasicBlock forBasicBlock = function.getCurBasicBlock();
            BasicBlock endForBasicBlock = new BasicBlock(null);
            BasicBlock afterForBasicBlock = new BasicBlock(null);
            stmt.analyze(symbolStack, function, endForBasicBlock, afterForBasicBlock);

            brInstruction = new BrUnconditional(function.getCurBasicBlock(), null);
            function.getCurBasicBlock().addInstruction(brInstruction);
            function.enterNextBasicBlock(endForBasicBlock);
            brInstruction.setBrBasicBlock(function.getCurBasicBlock());
            if (forStmt2 != null) {
                forStmt2.analyze(symbolStack, function);
            }
            function.getCurBasicBlock().addInstruction(new BrUnconditional(function.getCurBasicBlock(), forBasicBlock));
            function.enterNextBasicBlock(afterForBasicBlock);
        }
    }
}
