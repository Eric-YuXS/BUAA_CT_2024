package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.CallGetInt;
import LLVMIR.Instructions.Store;
import LLVMIR.Instructions.Trunc;
import frontend.SymbolStack;
import frontend.Token;

public class Stmt9 extends Stmt {  // Stmt → LVal '=' 'getint''('')'';'
    private final LVal lVal;
    private final Token assign;
    private final Token getint;
    private final Token lParent;
    private final Token rParent;
    private final Token semicn;

    public Stmt9(LVal lVal, Token assign, Token getint, Token lParent, Token rParent, Token semicn) {
        super();
        this.lVal = lVal;
        this.assign = assign;
        this.getint = getint;
        this.lParent = lParent;
        this.rParent = rParent;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lVal).append(assign).append(getint).append(lParent);
        if (rParent != null) {
            sb.append(rParent);
        }
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<Stmt>\n").toString();
    }

    @Override
    public void analyze(SymbolStack symbolStack, Function function, BasicBlock forCondBasicBlock, BasicBlock forEndBasicBlock) {
        Instruction callGetIntInstruction = new CallGetInt(function);
        function.getCurBasicBlock().addInstruction(callGetIntInstruction);
        Instruction lvalInstruction = lVal.analyze(symbolStack, function, true);
        if (lvalInstruction.getSymbolType().isI8()) {
            callGetIntInstruction = new Trunc(function, callGetIntInstruction);
            function.getCurBasicBlock().addInstruction(callGetIntInstruction);
        }
        function.getCurBasicBlock().addInstruction(new Store(function, lvalInstruction, callGetIntInstruction));
    }
}
