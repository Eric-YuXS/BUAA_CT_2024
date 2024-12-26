package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
import LLVMIR.Instruction;
import LLVMIR.Instructions.CallGetChar;
import LLVMIR.Instructions.Store;
import LLVMIR.Instructions.Trunc;
import frontend.FuncSymbol;
import frontend.SymbolStack;
import frontend.Token;

public class Stmt10 extends Stmt {  // Stmt → LVal '=' 'getchar''('')'';'
    private final LVal lVal;
    private final Token assign;
    private final Token getchar;
    private final Token lParent;
    private final Token rParent;
    private final Token semicn;

    public Stmt10(LVal lVal, Token assign, Token getchar, Token lParent, Token rParent, Token semicn) {
        super();
        this.lVal = lVal;
        this.assign = assign;
        this.getchar = getchar;
        this.lParent = lParent;
        this.rParent = rParent;
        this.semicn = semicn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lVal).append(assign).append(getchar).append(lParent);
        if (rParent != null) {
            sb.append(rParent);
        }
        if (semicn != null) {
            sb.append(semicn);
        }
        return sb.append("<Stmt>\n").toString();
    }

    @Override
    public void errorAnalyze(SymbolStack symbolStack, FuncSymbol funcSymbol, boolean isLoop) {
        lVal.errorAnalyze(symbolStack, true);
    }

    @Override
    public void analyze(SymbolStack symbolStack, Function function, BasicBlock forCondBasicBlock, BasicBlock forEndBasicBlock) {
        Instruction callGetCharInstruction = new CallGetChar(function);
        function.getCurBasicBlock().addInstruction(callGetCharInstruction);
        Instruction lvalInstruction = lVal.analyze(symbolStack, function, true);
        if (lvalInstruction.getSymbolType().isI8()) {
            callGetCharInstruction = new Trunc(function, callGetCharInstruction);
            function.getCurBasicBlock().addInstruction(callGetCharInstruction);
        }
        function.getCurBasicBlock().addInstruction(new Store(function, lvalInstruction, callGetCharInstruction));
    }
}
