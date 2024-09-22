package frontend;

public class Token {
    private TokenType type;
    private String string;

    public Token() {
        type = TokenType.UNKNOWN;
        string = "";
    }

    public TokenType getType() {
        return type;
    }

    public String getString() {
        return string;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public void catChar(char c) {
        string += c;
    }

    public String toString() {
        return type + " " + string;
    }
}
