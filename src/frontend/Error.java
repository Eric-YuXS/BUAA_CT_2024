package frontend;

public class Error implements Comparable {
    private final int line;
    private final char errorCode;

    public Error(int line, char errorCode) {
        this.line = line;
        this.errorCode = errorCode;
    }

    public String toString() {
        return line + " " + errorCode;
    }

    public int compareTo(Object o) {
        if (o instanceof Error) {
            if (((Error) o).line != line) {
                return line - ((Error) o).line;
            } else {
                return errorCode - ((Error) o).errorCode;
            }
        } else {
            return 0;
        }
    }
}
