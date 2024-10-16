package SyntaxTree;

public class UnaryExp3 extends UnaryExp {  // UnaryExp → UnaryOp UnaryExp
    private final UnaryOp unaryOp;
    private final UnaryExp unaryExp;

    public UnaryExp3(UnaryOp unaryOp, UnaryExp unaryExp) {
        super();
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    @Override
    public String toString() {
        return unaryOp.toString() + unaryExp + "<UnaryExp>\n";
    }
}
