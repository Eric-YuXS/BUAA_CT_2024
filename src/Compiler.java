import SyntaxTree.CompUnit;
import frontend.Lexer;
import frontend.Error;
import frontend.Parser;
import frontend.Token;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Compiler {
    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("testfile.txt"));
            BufferedWriter out = new BufferedWriter(new FileWriter("parser.txt"));
            BufferedWriter errorOut = new BufferedWriter(new FileWriter("error.txt"));
            Lexer lexer = new Lexer(in);
            lexer.run();
            ArrayList<Token> tokens = lexer.getTokens();
            ArrayList<Error> errors = lexer.getErrors();
            Parser parser = new Parser(tokens);
            CompUnit compUnit = parser.run();
            errors.addAll(parser.getErrors());
            if (errors.isEmpty()) {
                out.write(compUnit.toString());
            } else {
                Collections.sort(errors);
                for (Error error : errors) {
                    errorOut.write(error.toString() + "\n");
                }
            }
            in.close();
            out.close();
            errorOut.close();
        } catch (Exception e) {
        }
    }
}
