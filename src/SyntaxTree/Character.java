package SyntaxTree;

import frontend.Token;

public class Character implements SyntaxTreeNode {  // Character → CharConst
    private final Token charConst;

    public Character(Token charConst) {
        this.charConst = charConst;
    }

    public int getNum() {
        return charConst.getString().charAt(1) == '\\' ? getEscapeCharacter(charConst.getString().charAt(2)) :
                charConst.getString().charAt(1);
    }

    private static char getEscapeCharacter(char escape) {
        switch (escape) {
            case 'a':
                return 7;
            case 'b':
                return 8;
            case 't':
                return 9;
            case 'n':
                return 10;
            case 'v':
                return 11;
            case 'f':
                return 12;
            case 'r':
                return 13;
            case '0':
                return 0;
            case '\\':
                return 92;
            case '"':
                return 34;
            case '\'':
                return 39;
            default:
                return escape;
        }
    }

    @Override
    public String toString() {
        return charConst + "<Character>\n";
    }
}
