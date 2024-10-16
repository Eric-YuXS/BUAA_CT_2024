package SyntaxTree;

public class PrimaryExp3 extends PrimaryExp {  // PrimaryExp → Number
    private final Number number;

    public PrimaryExp3(Number number) {
        super();
        this.number = number;
    }

    @Override
    public String toString() {
        return number + "<PrimaryExp>\n";
    }
}
