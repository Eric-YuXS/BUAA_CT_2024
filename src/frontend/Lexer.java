package frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Lexer {
    private final BufferedReader in;
    private int lineNumber;
    private String line;
    private int index;
    private char charNow;
    private Token token;
    private final ArrayList<Token> tokens;
    private final ArrayList<Error> errors;
    private static final HashSet<String> keywords = new HashSet<>(Arrays.asList(
            "main", "const", "int", "char", "void", "break", "continue",
            "if", "else", "for", "getint", "getchar", "printf", "return"));

    public Lexer(BufferedReader in) {
        this.in = in;
        lineNumber = 0;
        line = "";
        tokens = new ArrayList<>();
        errors = new ArrayList<>();
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public ArrayList<Error> getErrors() {
        return errors;
    }

    public void run() throws IOException {
        while ((line = in.readLine()) != null) {
            lineNumber++;
            index = 0;
            while (index < line.length()) {
                token = new Token(lineNumber);
                do {
                    getCharNow();
                } while (Character.isWhitespace(charNow));
                if (Character.isLetter(charNow) || charNow == '_') {
                    do {
                        token.catChar(charNow);
                        getCharNow();
                    } while (Character.isLetterOrDigit(charNow) || charNow == '_');
                    retract();
                    if (keywords.contains(token.getString())) {
                        token.setType(TokenType.fromString(token.getString()));
                        tokens.add(token);
                    } else {
                        token.setType(TokenType.IDENFR);
                        tokens.add(token);
                    }
                } else if (Character.isDigit(charNow)) {
                    do {
                        token.catChar(charNow);
                        getCharNow();
                    } while (Character.isDigit(charNow));
                    retract();
                    token.setType(TokenType.INTCON);
                    tokens.add(token);
                } else if (charNow == '\"') {
                    do {
                        if (charNow == '\\') {
                            token.catChar(charNow);
                            getCharNow();
                        }
                        token.catChar(charNow);
                        getCharNow();
                    } while (charNow != '\"');
                    token.catChar(charNow);
                    token.setType(TokenType.STRCON);
                    tokens.add(token);
                } else if (charNow == '\'') {
                    do {
                        if (charNow == '\\') {
                            token.catChar(charNow);
                            getCharNow();
                        }
                        token.catChar(charNow);
                        getCharNow();
                    } while (charNow != '\'');
                    token.catChar(charNow);
                    token.setType(TokenType.CHRCON);
                    tokens.add(token);
                } else if (charNow == '!') {
                    token.catChar(charNow);
                    getCharNow();
                    if (charNow == '=') {
                        token.catChar(charNow);
                        token.setType(TokenType.NEQ);
                        tokens.add(token);
                    } else {
                        retract();
                        token.setType(TokenType.NOT);
                        tokens.add(token);
                    }
                } else if (charNow == '&') {
                    token.catChar(charNow);
                    getCharNow();
                    if (charNow == '&') {
                        token.catChar(charNow);
                        token.setType(TokenType.AND);
                        tokens.add(token);
                    } else {
                        retract();
                        token.setType(TokenType.AND);
                        tokens.add(token);
                        errors.add(new Error(lineNumber, 'a'));
                    }
                } else if (charNow == '|') {
                    token.catChar(charNow);
                    getCharNow();
                    if (charNow == '|') {
                        token.catChar(charNow);
                        token.setType(TokenType.OR);
                        tokens.add(token);
                    } else {
                        retract();
                        token.setType(TokenType.OR);
                        tokens.add(token);
                        errors.add(new Error(lineNumber, 'a'));
                    }
                } else if (charNow == '+') {
                    token.catChar(charNow);
                    token.setType(TokenType.PLUS);
                    tokens.add(token);
                } else if (charNow == '-') {
                    token.catChar(charNow);
                    token.setType(TokenType.MINU);
                    tokens.add(token);
                } else if (charNow == '*') {
                    token.catChar(charNow);
                    token.setType(TokenType.MULT);
                    tokens.add(token);
                } else if (charNow == '/') {
                    getCharNow();
                    if (charNow == '*') {
                        getCharNowThroughLines();
                        do {
                            while (charNow != '*') {
                                getCharNowThroughLines();
                            }
                            getCharNow();
                        } while (charNow != '/');
                    } else if (charNow == '/') {
                        index = line.length();
                    } else {
                        retract();
                        token.catChar('/');
                        token.setType(TokenType.DIV);
                        tokens.add(token);
                    }
                } else if (charNow == '%') {
                    token.catChar(charNow);
                    token.setType(TokenType.MOD);
                    tokens.add(token);
                } else if (charNow == '<') {
                    token.catChar(charNow);
                    getCharNow();
                    if (charNow == '=') {
                        token.catChar(charNow);
                        token.setType(TokenType.LEQ);
                        tokens.add(token);
                    } else {
                        retract();
                        token.setType(TokenType.LSS);
                        tokens.add(token);
                    }
                } else if (charNow == '>') {
                    token.catChar(charNow);
                    getCharNow();
                    if (charNow == '=') {
                        token.catChar(charNow);
                        token.setType(TokenType.GEQ);
                        tokens.add(token);
                    } else {
                        retract();
                        token.setType(TokenType.GRE);
                        tokens.add(token);
                    }
                } else if (charNow == '=') {
                    token.catChar(charNow);
                    getCharNow();
                    if (charNow == '=') {
                        token.catChar(charNow);
                        token.setType(TokenType.EQL);
                        tokens.add(token);
                    } else {
                        retract();
                        token.setType(TokenType.ASSIGN);
                        tokens.add(token);
                    }
                } else if (charNow == ';') {
                    token.catChar(charNow);
                    token.setType(TokenType.SEMICN);
                    tokens.add(token);
                } else if (charNow == ',') {
                    token.catChar(charNow);
                    token.setType(TokenType.COMMA);
                    tokens.add(token);
                } else if (charNow == '(') {
                    token.catChar(charNow);
                    token.setType(TokenType.LPARENT);
                    tokens.add(token);
                } else if (charNow == ')') {
                    token.catChar(charNow);
                    token.setType(TokenType.RPARENT);
                    tokens.add(token);
                } else if (charNow == '[') {
                    token.catChar(charNow);
                    token.setType(TokenType.LBRACK);
                    tokens.add(token);
                } else if (charNow == ']') {
                    token.catChar(charNow);
                    token.setType(TokenType.RBRACK);
                    tokens.add(token);
                } else if (charNow == '{') {
                    token.catChar(charNow);
                    token.setType(TokenType.LBRACE);
                    tokens.add(token);
                } else if (charNow == '}') {
                    token.catChar(charNow);
                    token.setType(TokenType.RBRACE);
                    tokens.add(token);
                }
            }
        }
    }

    private void getCharNow() {
        if (index > line.length()) {
            System.err.println("Line ended! Line: " + lineNumber + " Index: " + index);
        } else if (index == line.length()) {
            charNow = (char) -1;
            index++;
        } else {
            charNow = line.charAt(index);
            index++;
        }
    }

    private void getCharNowThroughLines() throws IOException {
        if (index == line.length()) {
            if ((line = in.readLine()) != null) {
                lineNumber++;
                index = 0;
                charNow = line.charAt(index);
                index++;
            } else {
                System.err.println("Text ended!");
            }
        } else {
            charNow = line.charAt(index);
            index++;
        }
    }

    private void retract() {
        if (index == 0) {
            System.err.println("Retract at the first char!");
        }
        index--;
    }
}
