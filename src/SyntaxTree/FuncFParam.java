package SyntaxTree;

import LLVMIR.Function;
import LLVMIR.Instructions.*;
import frontend.Error;
import frontend.Symbol;
import frontend.SymbolStack;
import frontend.SymbolType;
import frontend.Token;

public class FuncFParam implements SyntaxTreeNode {  // FuncFParam → BType Ident ['[' ']']
    private final BType bType;
    private final Token ident;
    private final Token lBrack;
    private final Token rBrack;

    public FuncFParam(BType bType, Token ident, Token lBrack, Token rBrack) {
        this.bType = bType;
        this.ident = ident;
        this.lBrack = lBrack;
        this.rBrack = rBrack;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(bType).append(ident);
        if (lBrack != null) {
            sb.append(lBrack);
            if (rBrack != null) {
                sb.append(rBrack);
            }
        }
        return sb.append("<FuncFParam>\n").toString();
    }

    public Symbol analyze(SymbolStack symbolStack, Function function, int paramIndex) {
        SymbolType symbolType = bType.getSymbolType();
        if (lBrack != null) {
            symbolType = symbolType.varToArray();
        }
        Symbol symbol = new Symbol(symbolType, ident.getString());
        if (!symbolStack.addSymbol(symbol)) {
            symbolStack.addError(new Error(ident.getLineNumber(), 'b'));
        }
        if (lBrack == null) {
            ParamVar paramVarInstruction = new ParamVar(function, symbol, paramIndex);
            function.addParamInstruction(paramVarInstruction);

            Var varInstruction = new Var(function, symbolType);
            function.getCurBasicBlock().addInstruction(varInstruction);
            symbol.setInstruction(varInstruction);

            function.getCurBasicBlock().addInstruction(new Store(function, varInstruction, paramVarInstruction));
        } else {
            ParamArray paramArrayInstruction = new ParamArray(function, symbol, paramIndex);
            function.addParamInstruction(paramArrayInstruction);
            symbol.setInstruction(paramArrayInstruction);
        }
        return symbol;
    }
}
