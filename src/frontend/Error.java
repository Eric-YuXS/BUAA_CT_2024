package frontend;

public class Error {
    private final int line;
    private final char errorCode;

    public Error(int line, char errorCode) {
        this.line = line;
        this.errorCode = errorCode;
    }

    public String toString() {
        return line + " " + errorCode;
    }
}
