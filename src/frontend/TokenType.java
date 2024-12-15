package frontend;

public enum TokenType {
    UNKNOWN, EOF,
    IDENFR, INTCON, STRCON, CHRCON,
    MAINTK, CONSTTK, INTTK, CHARTK, VOIDTK,
    BREAKTK, CONTINUETK, IFTK, ELSETK,
    NOT, AND, OR,
    FORTK, GETINTTK, GETCHARTK, PRINTFTK, RETURNTK,
    PLUS, MINU, MULT, DIV, MOD,
    LSS, LEQ, GRE, GEQ, EQL, NEQ,
    ASSIGN, SEMICN, COMMA,
    LPARENT, RPARENT, LBRACK, RBRACK, LBRACE, RBRACE;

    @Override
    public String toString() {
        switch (this) {
            case UNKNOWN:
                return "UNKNOWN";
            case EOF:
                return "EOF";
            case IDENFR:
                return "IDENFR";
            case INTCON:
                return "INTCON";
            case STRCON:
                return "STRCON";
            case CHRCON:
                return "CHRCON";
            case MAINTK:
                return "MAINTK";
            case CONSTTK:
                return "CONSTTK";
            case INTTK:
                return "INTTK";
            case CHARTK:
                return "CHARTK";
            case VOIDTK:
                return "VOIDTK";
            case BREAKTK:
                return "BREAKTK";
            case CONTINUETK:
                return "CONTINUETK";
            case IFTK:
                return "IFTK";
            case ELSETK:
                return "ELSETK";
            case NOT:
                return "NOT";
            case AND:
                return "AND";
            case OR:
                return "OR";
            case FORTK:
                return "FORTK";
            case GETINTTK:
                return "GETINTTK";
            case GETCHARTK:
                return "GETCHARTK";
            case PRINTFTK:
                return "PRINTFTK";
            case RETURNTK:
                return "RETURNTK";
            case PLUS:
                return "PLUS";
            case MINU:
                return "MINU";
            case MULT:
                return "MULT";
            case DIV:
                return "DIV";
            case MOD:
                return "MOD";
            case LSS:
                return "LSS";
            case LEQ:
                return "LEQ";
            case GRE:
                return "GRE";
            case GEQ:
                return "GEQ";
            case EQL:
                return "EQL";
            case NEQ:
                return "NEQ";
            case ASSIGN:
                return "ASSIGN";
            case SEMICN:
                return "SEMICN";
            case COMMA:
                return "COMMA";
            case LPARENT:
                return "LPARENT";
            case RPARENT:
                return "RPARENT";
            case LBRACK:
                return "LBRACK";
            case RBRACK:
                return "RBRACK";
            case LBRACE:
                return "LBRACE";
            case RBRACE:
                return "RBRACE";
            default:
                return "";
        }
    }

    public static TokenType fromString(String string) {
        switch (string) {
            case "const":
                return TokenType.CONSTTK;
            case "main":
                return TokenType.MAINTK;
            case "int":
                return TokenType.INTTK;
            case "char":
                return TokenType.CHARTK;
            case "void":
                return TokenType.VOIDTK;
            case "break":
                return TokenType.BREAKTK;
            case "continue":
                return TokenType.CONTINUETK;
            case "if":
                return TokenType.IFTK;
            case "else":
                return TokenType.ELSETK;
            case "for":
                return TokenType.FORTK;
            case "getint":
                return TokenType.GETINTTK;
            case "getchar":
                return TokenType.GETCHARTK;
            case "printf":
                return TokenType.PRINTFTK;
            case "return":
                return TokenType.RETURNTK;
            default:
                return null;
        }
    }

    public String getIcmpInstruction() {
        switch (this) {
            case LSS:
                return "slt";
            case LEQ:
                return "sle";
            case GRE:
                return "sgt";
            case GEQ:
                return "sge";
            case EQL:
                return "eq";
            case NEQ:
                return "ne";
            default:
                System.out.println("Token type cannot be used in icmp: " + this);
                return null;
        }
    }
}
