package SyntaxTree;

public class PrimaryExp implements SyntaxTreeNode {  // PrimaryExp → '(' Exp ')' | LVal | Number | Character
    @Override
    public String toString() {
        return "super<PrimaryExp>\n";
    }
}
