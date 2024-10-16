package SyntaxTree;

public class UnaryExp implements SyntaxTreeNode {  // UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    @Override
    public String toString() {
        return "super<UnaryExp>\n";
    }
}
