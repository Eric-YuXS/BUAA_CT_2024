package SyntaxTree;

public class PrimaryExp4 extends PrimaryExp {  // PrimaryExp → Character
    private final Character character;

    public PrimaryExp4(Character character) {
        super();
        this.character = character;
    }

    @Override
    public String toString() {
        return character + "<PrimaryExp>\n";
    }
}
