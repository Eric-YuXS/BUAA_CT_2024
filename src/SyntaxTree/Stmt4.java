package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
import LLVMIR.Instructions.BrConditional;
import LLVMIR.Instructions.BrUnconditional;
import frontend.FuncSymbol;
import frontend.SymbolStack;
import frontend.Token;

import java.util.ArrayList;

public class Stmt4 extends Stmt {  // Stmt → 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
    private final Token ifTk;
    private final Token lParent;
    private final Cond cond;
    private final Token rParent;
    private final Stmt stmt;
    private final Token elseTk;
    private final Stmt elseStmt;

    public Stmt4(Token ifTk, Token lParent, Cond cond, Token rParent, Stmt stmt, Token elseTk, Stmt elseStmt) {
        super();
        this.ifTk = ifTk;
        this.lParent = lParent;
        this.cond = cond;
        this.rParent = rParent;
        this.stmt = stmt;
        this.elseTk = elseTk;
        this.elseStmt = elseStmt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ifTk).append(lParent).append(cond);
        if (rParent != null) {
            sb.append(rParent);
        }
        sb.append(stmt);
        if (elseTk != null) {
            sb.append(elseTk).append(elseStmt);
        }
        return sb.append("<Stmt>\n").toString();
    }

    @Override
    public void errorAnalyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean isLoop) {
        stmt.errorAnalyze(symbolStack, funcSymbol, isLoop);
        if (elseStmt != null) {
            elseStmt.errorAnalyze(symbolStack, funcSymbol, isLoop);
        }
    }

    @Override
    public void analyze(SymbolStack symbolStack, Function function, BasicBlock forCondBasicBlock, BasicBlock forEndBasicBlock) {
        ArrayList<BrConditional> condBrInstructions = cond.analyze(symbolStack, function);
        function.enterNextBasicBlock();
        BasicBlock ifBasicBlock = function.getCurBasicBlock();

        stmt.analyze(symbolStack, function, forCondBasicBlock, forEndBasicBlock);

        BrUnconditional ifBrInstruction = new BrUnconditional(function.getCurBasicBlock(), null);
        function.getCurBasicBlock().addInstruction(ifBrInstruction);

        function.enterNextBasicBlock();
        for (BrConditional condBrInstruction : condBrInstructions) {
            condBrInstruction.setBrBasicBlock1(ifBasicBlock);
            condBrInstruction.setBrBasicBlock2(function.getCurBasicBlock());
        }
        if (elseStmt != null) {
            elseStmt.analyze(symbolStack, function, forCondBasicBlock, forEndBasicBlock);
            BrUnconditional elseBrInstruction = new BrUnconditional(function.getCurBasicBlock(), null);
            function.getCurBasicBlock().addInstruction(elseBrInstruction);
            function.enterNextBasicBlock();
            ifBrInstruction.setBrBasicBlock(function.getCurBasicBlock());
            elseBrInstruction.setBrBasicBlock(function.getCurBasicBlock());
        } else {
            ifBrInstruction.setBrBasicBlock(function.getCurBasicBlock());
        }
    }
}
