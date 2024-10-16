package SyntaxTree;

public class InitVal implements SyntaxTreeNode {  // InitVal → Exp | '{' [ Exp { ',' Exp } ] '}' | StringConst
    @Override
    public String toString() {
        return "super<InitVal>\n";
    }
}
