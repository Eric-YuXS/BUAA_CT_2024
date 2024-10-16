package SyntaxTree;

public class Exp implements SyntaxTreeNode {  // Exp → AddExp
    private final AddExp addExp;

    public Exp(AddExp addExp) {
        this.addExp = addExp;
    }

    @Override
    public String toString() {
        return addExp + "<Exp>\n";
    }
}
