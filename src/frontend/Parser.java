package frontend;

import java.util.ArrayList;

public class Parser {
    private final ArrayList<Token> tokens;
    private int index;
    private Token token;
    private String outputs;
    private final ArrayList<Error> errors;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        index = 0;
        outputs = "";
        errors = new ArrayList<>();
    }

    public String getOutputs() {
        return outputs;
    }

    public ArrayList<Error> getErrors() {
        return errors;
    }

    public void run() {
        parseCompUnit();
    }

    private void parseCompUnit() {  // CompUnit → {Decl} {FuncDef} MainFuncDef
        getNextToken();
        while (token.isType(TokenType.CONSTTK) || token.isType(TokenType.INTTK) || token.isType(TokenType.CHARTK)) {
            if (token.isType(TokenType.CONSTTK)) {
                retract(1);
                parseDecl();
            } else {
                getNextToken();
                if (token.isType(TokenType.IDENFR)) {
                    getNextToken();
                    if (token.isType(TokenType.LPARENT)) {
                        retract(3);
                        getNextToken();
                        break;
                    } else {
                        retract(3);
                        parseDecl();
                    }
                } else if (token.isType(TokenType.MAINTK)) {
                    retract(2);
                    getNextToken();
                    break;
                } else {
                    System.err.println("Error at token " + token + ": expected IDENFR");
                }
            }
            getNextToken();
        }
        while (token.isType(TokenType.VOIDTK) || token.isType(TokenType.INTTK) || token.isType(TokenType.CHARTK)) {
            if (token.isType(TokenType.VOIDTK)) {
                retract(1);
                parseFuncDef();
            } else {
                getNextToken();
                if (token.isType(TokenType.IDENFR)) {
                    retract(2);
                    parseFuncDef();
                } else if (token.isType(TokenType.MAINTK)) {
                    retract(1);
                    break;
                } else {
                    System.err.println("Error at token " + token + ": expected IDENFR");
                }
            }
            getNextToken();
        }
        retract(1);
        parseMainFuncDef();
        outputLine("<CompUnit>");
    }

    private void parseDecl() {  // Decl → ConstDecl | VarDecl
        getNextToken();
        if (token.isType(TokenType.CONSTTK)) {
            retract(1);
            parseConstDecl();
        } else {
            retract(1);
            parseVarDecl();
        }
    }

    private void parseConstDecl() {  // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
        getNextToken();
        if (token.isType(TokenType.CONSTTK)) {
            outputToken();
            parseBType();
            parseConstDef();
            getNextToken();
            while (token.isType(TokenType.COMMA)) {
                outputToken();
                parseConstDef();
                getNextToken();
            }
            if (token.isType(TokenType.SEMICN)) {
                outputToken();
            } else {
                errors.add(new Error(getLastTokenLineNumber(), 'i'));
                retract(1);
            }
        } else {
            System.err.println("Error at token " + token + ": expected CONSTTK");
        }
        outputLine("<ConstDecl>");
    }

    private void parseBType() {  // BType → 'int' | 'char'
        getNextToken();
        if (token.isType(TokenType.INTTK) || token.isType(TokenType.CHARTK)) {
            outputToken();
        } else {
            System.err.println("Error at token " + token + ": expected INTTK or CHARTK");
        }
    }

    private void parseConstDef() {  // ConstDef → Ident [ '[' ConstExp ']' ] '=' ConstInitVal
        getNextToken();
        if (token.isType(TokenType.IDENFR)) {
            outputToken();
            getNextToken();
            if (token.isType(TokenType.LBRACK)) {
                outputToken();
                parseConstExp();
                getNextToken();
                if (token.isType(TokenType.RBRACK)) {
                    outputToken();
                } else {
                    errors.add(new Error(getLastTokenLineNumber(), 'k'));
                    retract(1);
                }
                getNextToken();
            }
            if (token.isType(TokenType.ASSIGN)) {
                outputToken();
                parseConstInitVal();
            } else {
                System.err.println("Error at token " + token + ": expected ASSIGN");
            }
        } else {
            System.err.println("Error at token " + token + ": expected IDENFR");
        }
        outputLine("<ConstDef>");
    }

    private void parseConstExp() {  // ConstExp → AddExp
        parseAddExp();
        outputLine("<ConstExp>");
    }

    private void parseAddExp() {  // AddExp → MulExp | AddExp ('+' | '−') MulExp
        parseMulExp();
        getNextToken();
        while (token.isType(TokenType.PLUS) || token.isType(TokenType.MINU)) {
            outputLine("<AddExp>");
            outputToken();
            parseMulExp();
            getNextToken();
        }
        retract(1);
        outputLine("<AddExp>");
    }

    private void passParseAddExp() {
        do {
            passParseMulExp();
            getNextToken();
        } while (token.isType(TokenType.PLUS) || token.isType(TokenType.MINU));
        retract(1);
    }

    private void parseMulExp() {  // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
        parseUnaryExp();
        getNextToken();
        while (token.isType(TokenType.MULT) || token.isType(TokenType.DIV) || token.isType(TokenType.MOD)) {
            outputLine("<MulExp>");
            outputToken();
            parseUnaryExp();
            getNextToken();
        }
        retract(1);
        outputLine("<MulExp>");
    }

    private void passParseMulExp()  {
        do {
            passParseUnaryExp();
            getNextToken();
        } while (token.isType(TokenType.MULT) || token.isType(TokenType.DIV) || token.isType(TokenType.MOD));
        retract(1);
    }

    private void parseUnaryExp() {  // UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
        getNextToken();
        if (token.isType(TokenType.PLUS) || token.isType(TokenType.MINU) || token.isType(TokenType.NOT)) {
            retract(1);
            parseUnaryOp();
            parseUnaryExp();
        } else if (token.isType(TokenType.INTCON) || token.isType(TokenType.CHRCON) || token.isType(TokenType.LPARENT)) {
            retract(1);
            parsePrimaryExp();
        } else if (token.isType(TokenType.IDENFR)) {
            getNextToken();
            if (token.isType(TokenType.LPARENT)) {
                outputToken(tokens.get(index - 2));
                outputToken();
                getNextToken();
                if (token.isType(TokenType.RPARENT)) {
                    outputToken();
                } else {
                    retract(1);
                    parseFuncRParams();
                    getNextToken();
                    if (token.isType(TokenType.RPARENT)) {
                        outputToken();
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'j'));
                        retract(1);
                    }
                }
            } else {
                retract(2);
                parsePrimaryExp();
            }
        } else {
            System.err.println("Error at token " + token + ": expected IDENFR");
        }
        outputLine("<UnaryExp>");
    }

    private void passParseUnaryExp() {
        getNextToken();
        if (token.isType(TokenType.PLUS)) {
            System.out.println("here");
        }
        if (token.isType(TokenType.PLUS) || token.isType(TokenType.MINU) || token.isType(TokenType.NOT)) {
            retract(1);
            passParseUnaryOp();
            passParseUnaryExp();
        } else if (token.isType(TokenType.INTCON) || token.isType(TokenType.CHRCON) || token.isType(TokenType.LPARENT)) {
            retract(1);
            passParsePrimaryExp();
        } else if (token.isType(TokenType.IDENFR)) {
            getNextToken();
            if (token.isType(TokenType.LPARENT)) {
                getNextToken();
                if (!token.isType(TokenType.RPARENT)) {
                    retract(1);
                    passParseFuncRParams();
                    getNextToken();
                    if (!token.isType(TokenType.RPARENT)) {
                        retract(1);
                    }
                }
            } else {
                retract(2);
                passParsePrimaryExp();
            }
        } else {
            System.err.println("Error at token " + token + ": expected IDENFR");
        }
    }

    private void parsePrimaryExp() {  // PrimaryExp → '(' Exp ')' | LVal | Number | Character
        getNextToken();
        if (token.isType(TokenType.LPARENT)) {
            outputToken();
            parseExp();
            getNextToken();
            if (token.isType(TokenType.RPARENT)) {
                outputToken();
            } else {
                errors.add(new Error(getLastTokenLineNumber(), 'j'));
                retract(1);
            }
        } else if (token.isType(TokenType.IDENFR)) {
            retract(1);
            parseLVal();
        } else if (token.isType(TokenType.INTCON)) {
            retract(1);
            parseNumber();
        } else if (token.isType(TokenType.CHRCON)) {
            retract(1);
            parseCharacter();
        } else {
            System.err.println("Error at token " + token + ": expected LPARENT or IDENFR or INTCON or CHRCON");
        }
        outputLine("<PrimaryExp>");
    }

    private void passParsePrimaryExp() {
        getNextToken();
        if (token.isType(TokenType.LPARENT)) {
            passParseExp();
            getNextToken();
            if (!token.isType(TokenType.RPARENT)) {
                retract(1);
            }
        } else if (token.isType(TokenType.IDENFR)) {
            retract(1);
            passParseLVal();
        } else if (token.isType(TokenType.INTCON)) {
            retract(1);
            passParseNumber();
        } else if (token.isType(TokenType.CHRCON)) {
            retract(1);
            passParseCharacter();
        } else {
            System.err.println("Error at token " + token + ": expected LPARENT or IDENFR or INTCON or CHRCON");
        }
    }

    private void parseExp() {  // Exp → AddExp
        parseAddExp();
        outputLine("<Exp>");
    }

    private void passParseExp() {
        passParseAddExp();
    }

    private void parseLVal() {  // LVal → Ident ['[' Exp ']']
        getNextToken();
        if (token.isType(TokenType.IDENFR)) {
            outputToken();
            getNextToken();
            if (token.isType(TokenType.LBRACK)) {
                outputToken();
                parseExp();
                getNextToken();
                if (token.isType(TokenType.RBRACK)) {
                    outputToken();
                } else {
                    errors.add(new Error(getLastTokenLineNumber(), 'k'));
                    retract(1);
                }
            } else {
                retract(1);
            }
        } else {
            System.err.println("Error at token " + token + ": expected IDENFR");
        }
        outputLine("<LVal>");
    }

    private void passParseLVal() {
        getNextToken();
        if (token.isType(TokenType.IDENFR)) {
            getNextToken();
            if (token.isType(TokenType.LBRACK)) {
                passParseExp();
                getNextToken();
                if (!token.isType(TokenType.RBRACK)) {
                    retract(1);
                }
            } else {
                retract(1);
            }
        } else {
            System.err.println("Error at token " + token + ": expected IDENFR");
        }
    }

    private void parseNumber() {  // Number → IntConst
        getNextToken();
        if (token.isType(TokenType.INTCON)) {
            outputToken();
        } else {
            System.err.println("Error at token " + token + ": expected INTCON");
        }
        outputLine("<Number>");
    }

    private void passParseNumber() {
        getNextToken();
        if (!token.isType(TokenType.INTCON)) {
            System.err.println("Error at token " + token + ": expected INTCON");
        }
    }

    private void parseCharacter() {  // Character → CharConst
        getNextToken();
        if (token.isType(TokenType.CHRCON)) {
            outputToken();
        } else {
            System.err.println("Error at token " + token + ": expected CHRCON");
        }
        outputLine("<Character>");
    }

    private void passParseCharacter() {
        getNextToken();
        if (!token.isType(TokenType.CHRCON)) {
            System.err.println("Error at token " + token + ": expected CHRCON");
        }
    }

    private void parseFuncRParams() {  // FuncRParams → Exp { ',' Exp }
        parseExp();
        getNextToken();
        while (token.isType(TokenType.COMMA)) {
            outputToken();
            parseExp();
            getNextToken();
        }
        retract(1);
        outputLine("<FuncRParams>");
    }

    private void passParseFuncRParams() {
        do {
            passParseExp();
            getNextToken();
        } while (token.isType(TokenType.COMMA));
        retract(1);
    }

    private void parseUnaryOp() {  // UnaryOp → '+' | '−' | '!'
        getNextToken();
        if (token.isType(TokenType.PLUS) || token.isType(TokenType.MINU) || token.isType(TokenType.NOT)) {
            outputToken();
        } else {
            System.err.println("Error at token " + token + ": expected PLUS or MINU or NOT");
        }
        outputLine("<UnaryOp>");
    }

    private void passParseUnaryOp() {
        getNextToken();
        if (!token.isType(TokenType.PLUS) && !token.isType(TokenType.MINU) && !token.isType(TokenType.NOT)) {
            System.err.println("Error at token " + token + ": expected PLUS or MINU or NOT");
        }
    }

    private void parseConstInitVal() {  // ConstInitVal → ConstExp | '{' [ ConstExp { ',' ConstExp } ] '}' | StringConst
        getNextToken();
        if (token.isType(TokenType.LBRACE)) {
            outputToken();
            getNextToken();
            if (token.isType(TokenType.RBRACE)) {
                outputToken();
            } else {
                retract(1);
                parseConstExp();
                getNextToken();
                while (token.isType(TokenType.COMMA)) {
                    outputToken();
                    parseConstExp();
                    getNextToken();
                }
                if (token.isType(TokenType.RBRACE)) {
                    outputToken();
                } else {
                    System.err.println("Error at token " + token + ": expected RBRACE");
                }
            }
        } else if (!token.isType(TokenType.STRCON)) {
            retract(1);
            parseConstExp();
        } else {
            outputToken();
        }
        outputLine("<ConstInitVal>");
    }

    private void parseVarDecl() {  // VarDecl → BType VarDef { ',' VarDef } ';'
        parseBType();
        parseVarDef();
        getNextToken();
        while (token.isType(TokenType.COMMA)) {
            outputToken();
            parseVarDef();
            getNextToken();
        }
        if (token.isType(TokenType.SEMICN)) {
            outputToken();
        } else {
            errors.add(new Error(getLastTokenLineNumber(), 'i'));
            retract(1);
        }
        outputLine("<VarDecl>");
    }

    private void parseVarDef() {  // VarDef → Ident [ '[' ConstExp ']' ] | Ident [ '[' ConstExp ']' ] '=' InitVal
        getNextToken();
        if (token.isType(TokenType.IDENFR)) {
            outputToken();
            getNextToken();
            if (token.isType(TokenType.LBRACK)) {
                outputToken();
                parseConstExp();
                getNextToken();
                if (token.isType(TokenType.RBRACK)) {
                    outputToken();
                    getNextToken();
                } else {
                    errors.add(new Error(getLastTokenLineNumber(), 'k'));
                }
            }
            if (token.isType(TokenType.ASSIGN)) {
                outputToken();
                parseInitVal();
            } else {
                retract(1);
            }
        } else {
            System.err.println("Error at token " + token + ": expected IDENFR");
        }
        outputLine("<VarDef>");
    }

    private void parseInitVal() {  // InitVal → Exp | '{' [ Exp { ',' Exp } ] '}' | StringConst
        getNextToken();
        if (token.isType(TokenType.LBRACE)) {
            outputToken();
            getNextToken();
            if (token.isType(TokenType.RBRACE)) {
                outputToken();
            } else {
                retract(1);
                parseExp();
                getNextToken();
                while (token.isType(TokenType.COMMA)) {
                    outputToken();
                    parseExp();
                    getNextToken();
                }
                if (token.isType(TokenType.RBRACE)) {
                    outputToken();
                } else {
                    System.err.println("Error at token " + token + ": expected RBRACE");
                }
            }
        } else if (!token.isType(TokenType.STRCON)) {
            retract(1);
            parseExp();
        } else {
            outputToken();
        }
        outputLine("<InitVal>");
    }

    private void parseFuncDef() {  // FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
        parseFuncType();
        getNextToken();
        if (token.isType(TokenType.IDENFR)) {
            outputToken();
            getNextToken();
            if (token.isType(TokenType.LPARENT)) {
                outputToken();
                getNextToken();
                if (token.isType(TokenType.RPARENT)) {
                    outputToken();
                } else {
                    retract(1);
                    parseFuncFParams();
                    getNextToken();
                    if (token.isType(TokenType.RPARENT)) {
                        outputToken();
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'j'));
                        retract(1);
                    }

                }
                parseBlock();
            } else {
                System.err.println("Error at token " + token + ": expected LBRACE");
            }
        } else {
            System.err.println("Error at token " + token + ": expected IDENFR");
        }
        outputLine("<FuncDef>");
    }

    private void parseFuncType() {  // FuncType → 'void' | 'int' | 'char'
        getNextToken();
        if (token.isType(TokenType.VOIDTK) || token.isType(TokenType.INTTK) || token.isType(TokenType.CHARTK)) {
            outputToken();
        } else {
            System.err.println("Error at token " + token + ": expected VOIDTK or INTTK or CHARTK");
        }
        outputLine("<FuncType>");
    }

    private void parseFuncFParams() {  // FuncFParams → FuncFParam { ',' FuncFParam }
        parseFuncFParam();
        getNextToken();
        while (token.isType(TokenType.COMMA)) {
            outputToken();
            parseFuncFParam();
            getNextToken();
        }
        retract(1);
        outputLine("<FuncFParams>");
    }

    private void parseFuncFParam() {  // FuncFParam → BType Ident ['[' ']']
        parseBType();
        getNextToken();
        if (token.isType(TokenType.IDENFR)) {
            outputToken();
            getNextToken();
            if (token.isType(TokenType.LBRACK)) {
                outputToken();
                getNextToken();
                if (token.isType(TokenType.RBRACK)) {
                    outputToken();
                } else {
                    errors.add(new Error(getLastTokenLineNumber(), 'k'));
                    retract(1);
                }
            } else {
                retract(1);
            }
        } else {
            System.err.println("Error at token " + token + ": expected IDENFR");
        }
        outputLine("<FuncFParam>");
    }

    private void parseBlock() {  // Block → '{' { BlockItem } '}'
        getNextToken();
        if (token.isType(TokenType.LBRACE)) {
            outputToken();
            getNextToken();
            while (!token.isType(TokenType.RBRACE) && !token.isType(TokenType.EOF)) {
                retract(1);
                parseBlockItem();
                getNextToken();
            }
            if (token.isType(TokenType.RBRACE)) {
                outputToken();
            } else {
                System.err.println("Error at token " + token + ": expected RBRACE");
            }
        } else {
            System.err.println("Error at token " + token + ": expected LBRACE");
        }
        outputLine("<Block>");
    }

    private void parseBlockItem() {  // BlockItem → Decl | Stmt
        getNextToken();
        if (token.isType(TokenType.CONSTTK) || token.isType(TokenType.INTTK) || token.isType(TokenType.CHARTK)) {
            retract(1);
            parseDecl();
        } else {
            retract(1);
            parseStmt();
        }
    }

    /* Stmt → LVal '=' Exp ';'
            | [Exp] ';'
            | Block
			| 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
            | 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
			| 'break' ';'
			| 'continue' ';'
            | 'return' [Exp] ';'
            | LVal '=' 'getint''('')'';'
            | LVal '=' 'getchar''('')'';'
            | 'printf''('StringConst {','Exp}')'';'
    */
    private void parseStmt() {
        getNextToken();
        if (token.isType(TokenType.LBRACE)) {
            retract(1);
            parseBlock();
        } else if (token.isType(TokenType.IFTK)) {
            outputToken();
            getNextToken();
            if (token.isType(TokenType.LPARENT)) {
                outputToken();
                parseCond();
                getNextToken();
                if (token.isType(TokenType.RPARENT)) {
                    outputToken();
                } else {
                    errors.add(new Error(getLastTokenLineNumber(), 'j'));
                    retract(1);
                }
                parseStmt();
                getNextToken();
                if (token.isType(TokenType.ELSETK)) {
                    outputToken();
                    parseStmt();
                } else {
                    retract(1);
                }
            } else {
                System.err.println("Error at token " + token + ": expected LPARENT");
            }
        } else if (token.isType(TokenType.FORTK)) {
            outputToken();
            getNextToken();
            if (token.isType(TokenType.LPARENT)) {
                outputToken();
                getNextToken();
                if (token.isType(TokenType.SEMICN)) {
                    outputToken();
                } else {
                    retract(1);
                    parseForStmt();
                    getNextToken();
                    if (token.isType(TokenType.SEMICN)) {
                        outputToken();
                    } else {
                        System.err.println("Error at token " + token + ": expected SEMICN");
                    }
                }
                getNextToken();
                if (token.isType(TokenType.SEMICN)) {
                    outputToken();
                } else {
                    retract(1);
                    parseCond();
                    getNextToken();
                    if (token.isType(TokenType.SEMICN)) {
                        outputToken();
                    } else {
                        System.err.println("Error at token " + token + ": expected SEMICN");
                    }
                }
                getNextToken();
                if (token.isType(TokenType.RPARENT)) {
                    outputToken();
                } else {
                    retract(1);
                    parseForStmt();
                    getNextToken();
                    if (token.isType(TokenType.RPARENT)) {
                        outputToken();
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'j'));
                        retract(1);
                    }
                }
                parseStmt();
            } else {
                System.err.println("Error at token " + token + ": expected LPARENT");
            }
        } else if (token.isType(TokenType.BREAKTK) || token.isType(TokenType.CONTINUETK)) {
            outputToken();
            getNextToken();
            if (token.isType(TokenType.SEMICN)) {
                outputToken();
            } else {
                errors.add(new Error(getLastTokenLineNumber(), 'i'));
                retract(1);
            }
        } else if (token.isType(TokenType.RETURNTK)) {
            outputToken();
            getNextToken();
            if (token.isType(TokenType.SEMICN)) {
                outputToken();
            } else {
                retract(1);
                parseExp();
                getNextToken();
                if (token.isType(TokenType.SEMICN)) {
                    outputToken();
                } else {
                    errors.add(new Error(getLastTokenLineNumber(), 'i'));
                    retract(1);
                }
            }
        } else if (token.isType(TokenType.PRINTFTK)) {
            outputToken();
            getNextToken();
            if (token.isType(TokenType.LPARENT)) {
                outputToken();
                getNextToken();
                if (token.isType(TokenType.STRCON)) {
                    outputToken();
                    getNextToken();
                    while (token.isType(TokenType.COMMA)) {
                        outputToken();
                        parseExp();
                        getNextToken();
                    }
                    if (token.isType(TokenType.RPARENT)) {
                        outputToken();
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'j'));
                        retract(1);
                    }
                    getNextToken();
                    if (token.isType(TokenType.SEMICN)) {
                        outputToken();
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'i'));
                        retract(1);
                    }
                } else {
                    System.err.println("Error at token " + token + ": expected STRCON");
                }
            } else {
                System.err.println("Error at token " + token + ": expected LPARENT");
            }
        } else if (token.isType(TokenType.SEMICN)) {
            outputToken();
        } else if (token.isType(TokenType.IDENFR)) {
            retract(1);
            int oriIndex = index;
            passParseLVal();
            getNextToken();
            setIndex(oriIndex);
            if (token.isType(TokenType.ASSIGN)) {
                parseLVal();
                getNextToken();
                if (token.isType(TokenType.ASSIGN)) {
                    outputToken();
                    getNextToken();
                    if (token.isType(TokenType.GETINTTK) || token.isType(TokenType.GETCHARTK)) {
                        outputToken();
                        getNextToken();
                        if (token.isType(TokenType.LPARENT)) {
                            outputToken();
                            getNextToken();
                            if (token.isType(TokenType.RPARENT)) {
                                outputToken();
                            } else {
                                errors.add(new Error(getLastTokenLineNumber(), 'j'));
                                retract(1);
                            }
                        } else {
                            System.err.println("Error at token " + token + ": expected LPARENT");
                        }
                    } else {
                        retract(1);
                        parseExp();
                    }
                    getNextToken();
                    if (token.isType(TokenType.SEMICN)) {
                        outputToken();
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'i'));
                        retract(1);
                    }
                } else {
                    System.err.println("Error at token " + token + ": expected ASSIGN");
                }
            } else {
                parseExp();
                getNextToken();
                if (token.isType(TokenType.SEMICN)) {
                    outputToken();
                } else {
                    errors.add(new Error(getLastTokenLineNumber(), 'i'));
                    retract(1);
                }
            }
        } else {
            retract(1);
            parseExp();
            getNextToken();
            if (token.isType(TokenType.SEMICN)) {
                outputToken();
            } else {
                errors.add(new Error(getLastTokenLineNumber(), 'i'));
                retract(1);
            }
        }
        outputLine("<Stmt>");
    }

    private void parseCond() {  // Cond → LOrExp
        parseLOrExp();
        outputLine("<Cond>");
    }

    private void parseLOrExp() {  // LOrExp → LAndExp | LOrExp '||' LAndExp
        parseLAndExp();
        getNextToken();
        while (token.isType(TokenType.OR)) {
            outputLine("<LOrExp>");
            outputToken();
            parseLAndExp();
            getNextToken();
        }
        retract(1);
        outputLine("<LOrExp>");
    }

    private void parseLAndExp() {  // LAndExp → EqExp | LAndExp '&&' EqExp
        parseEqExp();
        getNextToken();
        while (token.isType(TokenType.AND)) {
            outputLine("<LAndExp>");
            outputToken();
            parseEqExp();
            getNextToken();
        }
        retract(1);
        outputLine("<LAndExp>");
    }

    private void parseEqExp() {  // EqExp → RelExp | EqExp ('==' | '!=') RelExp
        parseRelExp();
        getNextToken();
        while (token.isType(TokenType.EQL) || token.isType(TokenType.NEQ)) {
            outputLine("<EqExp>");
            outputToken();
            parseRelExp();
            getNextToken();
        }
        retract(1);
        outputLine("<EqExp>");
    }

    private void parseRelExp() {  // RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
        parseAddExp();
        getNextToken();
        while (token.isType(TokenType.LSS) || token.isType(TokenType.GRE) || token.isType(TokenType.LEQ) || token.isType(TokenType.GEQ)) {
            outputLine("<RelExp>");
            outputToken();
            parseAddExp();
            getNextToken();
        }
        retract(1);
        outputLine("<RelExp>");
    }

    private void parseForStmt() {  // ForStmt → LVal '=' Exp
        parseLVal();
        getNextToken();
        if (token.isType(TokenType.ASSIGN)) {
            outputToken();
            parseExp();
        } else {
            System.err.println("Error at token " + token + ": expected ASSIGN");
        }
        outputLine("<ForStmt>");
    }

    private void parseMainFuncDef() {  // MainFuncDef → 'int' 'main' '(' ')' Block
        getNextToken();
        if (token.isType(TokenType.INTTK)) {
            outputToken();
            getNextToken();
            if (token.isType(TokenType.MAINTK)) {
                outputToken();
                getNextToken();
                if (token.isType(TokenType.LPARENT)) {
                    outputToken();
                    getNextToken();
                    if (token.isType(TokenType.RPARENT)) {
                        outputToken();
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'j'));
                        retract(1);
                    }
                    parseBlock();
                } else {
                    System.err.println("Error at token " + token + ": expected LPARENT");
                }
            } else {
                System.err.println("Error at token " + token + ": expected MAINTK");
            }
        } else {
            System.err.println("Error at token " + token + ": expected INTTK");
        }
        outputLine("<MainFuncDef>");
    }

    private void getNextToken() {
        if (index >= tokens.size()) {
            token = new Token(TokenType.EOF, "EOF", -1);
        } else {
            token = tokens.get(index);
        }
        index++;
    }

    private int getLastTokenLineNumber() {
        return tokens.get(index - 2).getLineNumber();
    }

    private void retract(int count) {
        index -= count;
    }

    private void setIndex(int index) {
        this.index = index;
    }

    private void outputToken() {
        outputs += token + "\n";
    }

    private void outputToken(Token token) {
        outputs += token + "\n";
    }

    private void outputLine(String line) {
        outputs += line + "\n";
    }
}
