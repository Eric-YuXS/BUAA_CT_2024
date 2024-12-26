package SyntaxTree;

import LLVMIR.BasicBlock;
import LLVMIR.Function;
import LLVMIR.Instructions.*;
import frontend.*;
import frontend.Error;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Stmt11 extends Stmt {  // Stmt → 'printf''('StringConst {','Exp}')'';'
    private final Token printf;
    private final Token lParent;
    private final Token stringConst;
    private final String[] strings;
    private final ArrayList<String> normalStringParts;
    private final ArrayList<String> formatStringParts;
    private final ArrayList<Exp> exps;
    private final ArrayList<Token> commas;
    private final Token rParent;
    private final Token semicn;

    public Stmt11(Token printf, Token lParent, Token stringConst, ArrayList<Exp> exps,
                  ArrayList<Token> commas, Token rParent, Token semicn) {
        super();
        this.printf = printf;
        this.lParent = lParent;
        this.stringConst = stringConst;
        strings = stringConst.getString().split("%d|%c");
        normalStringParts = new ArrayList<>();
        formatStringParts = new ArrayList<>();
        this.exps = exps;
        this.commas = commas;
        this.rParent = rParent;
        this.semicn = semicn;
        setStrings(stringConst.getString());
    }

    private void setStrings(String string) {
        String formatString = string.substring(1, string.length() - 1);
        Pattern pattern = Pattern.compile("%[dc]");
        Matcher matcher = pattern.matcher(formatString);
        int lastEnd = 0;
        while (matcher.find()) {
            normalStringParts.add(formatString.substring(lastEnd, matcher.start()));
            formatStringParts.add(matcher.group());
            lastEnd = matcher.end();
        }
        if (lastEnd <= formatString.length()) {
            normalStringParts.add(formatString.substring(lastEnd));
        } else {
            normalStringParts.add("");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(printf).append(lParent).append(stringConst);
        int expIndex = 0;
        for (Token comma : commas) {
            sb.append(comma).append(exps.get(expIndex++));
        }
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
        if (strings.length - 1 != exps.size()) {
            symbolStack.addError(new Error(printf.getLineNumber(), 'l'));
        }
        for (Exp exp : exps) {
            if (exp.errorAnalyze(symbolStack) == SymbolType.VoidFunc) {
                System.err.println("Print void!");
            }
        }
    }

    @Override
    public void analyze(SymbolStack symbolStack, Function function, BasicBlock forCondBasicBlock, BasicBlock forEndBasicBlock) {
        if (formatStringParts.size() != exps.size()) {
            symbolStack.addError(new Error(printf.getLineNumber(), 'l'));
        }
        for (String string : normalStringParts) {
            if (!string.isEmpty() && !function.getModule().containsStringInstruction(string)) {
                function.getModule().addStringInstruction(string, new AllocaPrintString(function.getModule(), string));
            }
        }
        if (!normalStringParts.get(0).isEmpty()) {
            function.getCurBasicBlock().addInstruction(new CallPutStr(function,
                    function.getModule().getStringInstruction(normalStringParts.get(0))));
        }
        for (int i = 0; i < formatStringParts.size(); i++) {
            if (formatStringParts.get(i).equals("%d")) {
                function.getCurBasicBlock().addInstruction(new CallPutInt(function, exps.get(i).analyze(symbolStack, function)));
            } else if (formatStringParts.get(i).equals("%c")) {
                function.getCurBasicBlock().addInstruction(new CallPutChar(function, exps.get(i).analyze(symbolStack, function)));
            } else {
                System.err.println("Unexpected format character: " + formatStringParts.get(i));
            }
            if (!normalStringParts.get(i + 1).isEmpty()) {
                function.getCurBasicBlock().addInstruction(new CallPutStr(function,
                        function.getModule().getStringInstruction(normalStringParts.get(i + 1))));
            }
        }
    }
}
