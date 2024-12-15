package frontend;

public class Token {
    private TokenType type;
    private String string;
    private final int lineNumber;

    public Token(int lineNumber) {
        type = TokenType.UNKNOWN;
        string = "";
        this.lineNumber = lineNumber;
    }

    public Token(TokenType type, String string, int lineNumber) {
        this.type = type;
        this.string = string;
        this.lineNumber = lineNumber;
    }

    public boolean isType(TokenType type) {
        return this.type == type;
    }

    public String getString() {
        return string;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public TokenType getType() {
        return type;
    }

    public void catChar(char c) {
        string += c;
    }

    public String toString() {
        return type + " " + string + "\n";
    }
}
