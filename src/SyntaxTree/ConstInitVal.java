package SyntaxTree;

public class ConstInitVal implements SyntaxTreeNode {  // ConstInitVal → ConstExp | '{' [ ConstExp { ',' ConstExp } ] '}' | StringConst
    @Override
    public String toString() {
        return "super<ConstInitVal>\n";
    }
}
