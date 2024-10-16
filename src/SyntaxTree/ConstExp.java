package SyntaxTree;

public class ConstExp implements SyntaxTreeNode {  // ConstExp → AddExp
    private final AddExp addExp;

    public ConstExp(AddExp addExp) {
        this.addExp = addExp;
    }

    @Override
    public String toString() {
        return addExp + "<ConstExp>\n";
    }
}
