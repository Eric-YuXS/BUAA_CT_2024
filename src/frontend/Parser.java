package frontend;

import SyntaxTree.*;
import SyntaxTree.Character;
import SyntaxTree.Number;

import java.util.ArrayList;

public class Parser {
    private final ArrayList<Token> tokens;
    private int index;
    private Token token;
    private final ArrayList<Error> errors;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        index = 0;
        errors = new ArrayList<>();
    }

    public ArrayList<Error> getErrors() {
        return errors;
    }

    public CompUnit run() {
        return parseCompUnit();
    }

    private CompUnit parseCompUnit() {  // CompUnit → {Decl} {FuncDef} MainFuncDef
        ArrayList<Decl> decls = new ArrayList<>();
        ArrayList<FuncDef> funcDefs = new ArrayList<>();
        getNextToken();
        while (token.isType(TokenType.CONSTTK) || token.isType(TokenType.INTTK) || token.isType(TokenType.CHARTK)) {
            if (token.isType(TokenType.CONSTTK)) {
                retract(1);
                decls.add(parseDecl());
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
                        decls.add(parseDecl());
                    }
                } else if (token.isType(TokenType.MAINTK)) {
                    retract(2);
                    getNextToken();
                    break;
                } else {
                    System.err.println("Error at token " + token + ": expected IDENFR");
                    return null;
                }
            }
            getNextToken();
        }
        while (token.isType(TokenType.VOIDTK) || token.isType(TokenType.INTTK) || token.isType(TokenType.CHARTK)) {
            if (token.isType(TokenType.VOIDTK)) {
                retract(1);
                funcDefs.add(parseFuncDef());
            } else {
                getNextToken();
                if (token.isType(TokenType.IDENFR)) {
                    retract(2);
                    funcDefs.add(parseFuncDef());
                } else if (token.isType(TokenType.MAINTK)) {
                    retract(1);
                    break;
                } else {
                    System.err.println("Error at token " + token + ": expected IDENFR");
                    return null;
                }
            }
            getNextToken();
        }
        retract(1);
        return new CompUnit(decls, funcDefs, parseMainFuncDef());
    }

    private Decl parseDecl() {  // Decl → ConstDecl | VarDecl
        getNextToken();
        if (token.isType(TokenType.CONSTTK)) {
            retract(1);
            return new Decl1(parseConstDecl());
        } else {
            retract(1);
            return new Decl2(parseVarDecl());
        }
    }

    private ConstDecl parseConstDecl() {  // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
        getNextToken();
        if (token.isType(TokenType.CONSTTK)) {
            Token constTk = token;
            BType bType = parseBType();
            ArrayList<ConstDef> constDefs = new ArrayList<>();
            ArrayList<Token> commas = new ArrayList<>();
            constDefs.add(parseConstDef());
            getNextToken();
            while (token.isType(TokenType.COMMA)) {
                commas.add(token);
                constDefs.add(parseConstDef());
                getNextToken();
            }
            Token semicn = null;
            if (token.isType(TokenType.SEMICN)) {
                semicn = token;
            } else {
                errors.add(new Error(getLastTokenLineNumber(), 'i'));
                retract(1);
            }
            return new ConstDecl(constTk, bType, constDefs, commas, semicn);
        } else {
            System.err.println("Error at token " + token + ": expected CONSTTK");
            return null;
        }
    }

    private BType parseBType() {  // BType → 'int' | 'char'
        getNextToken();
        if (token.isType(TokenType.INTTK) || token.isType(TokenType.CHARTK)) {
            return new BType(token);
        } else {
            System.err.println("Error at token " + token + ": expected INTTK or CHARTK");
            return null;
        }
    }

    private ConstDef parseConstDef() {  // ConstDef → Ident [ '[' ConstExp ']' ] '=' ConstInitVal
        getNextToken();
        if (token.isType(TokenType.IDENFR)) {
            Token ident = token;
            Token lBrack = null;
            ConstExp constExp = null;
            Token rBrack = null;
            getNextToken();
            if (token.isType(TokenType.LBRACK)) {
                lBrack = token;
                constExp = parseConstExp();
                getNextToken();
                if (token.isType(TokenType.RBRACK)) {
                    rBrack = token;
                } else {
                    errors.add(new Error(getLastTokenLineNumber(), 'k'));
                    retract(1);
                }
                getNextToken();
            }
            if (token.isType(TokenType.ASSIGN)) {
                return new ConstDef(ident, lBrack, constExp, rBrack, token, parseConstInitVal());
            } else {
                System.err.println("Error at token " + token + ": expected ASSIGN");
                return null;
            }
        } else {
            System.err.println("Error at token " + token + ": expected IDENFR");
            return null;
        }
    }

    private ConstExp parseConstExp() {  // ConstExp → AddExp
        return new ConstExp(parseAddExp());
    }

    private AddExp parseAddExp() {  // AddExp → MulExp | AddExp ('+' | '−') MulExp
        ArrayList<MulExp> multiExps = new ArrayList<>();
        ArrayList<Token> operators = new ArrayList<>();
        multiExps.add(parseMulExp());
        getNextToken();
        while (token.isType(TokenType.PLUS) || token.isType(TokenType.MINU)) {
            operators.add(token);
            multiExps.add(parseMulExp());
            getNextToken();
        }
        retract(1);
        return new AddExp(multiExps, operators);
    }

    private AddExp tryParseAddExp() {
        ArrayList<MulExp> multiExps = new ArrayList<>();
        ArrayList<Token> operators = new ArrayList<>();
        MulExp mulExp = tryParseMulExp();
        if (mulExp != null) {
            multiExps.add(mulExp);
        } else {
            return null;
        }
        getNextToken();
        while (token.isType(TokenType.PLUS) || token.isType(TokenType.MINU)) {
            operators.add(token);
            mulExp = tryParseMulExp();
            if (mulExp != null) {
                multiExps.add(mulExp);
            } else {
                return null;
            }
            getNextToken();
        }
        retract(1);
        return new AddExp(multiExps, operators);
    }

    private MulExp parseMulExp() {  // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
        ArrayList<UnaryExp> unaryExps = new ArrayList<>();
        ArrayList<Token> operators = new ArrayList<>();
        unaryExps.add(parseUnaryExp());
        getNextToken();
        while (token.isType(TokenType.MULT) || token.isType(TokenType.DIV) || token.isType(TokenType.MOD)) {
            operators.add(token);
            unaryExps.add(parseUnaryExp());
            getNextToken();
        }
        retract(1);
        return new MulExp(unaryExps, operators);
    }

    private MulExp tryParseMulExp() {
        ArrayList<UnaryExp> unaryExps = new ArrayList<>();
        ArrayList<Token> operators = new ArrayList<>();
        UnaryExp unaryExp = tryParseUnaryExp();
        if (unaryExp != null) {
            unaryExps.add(unaryExp);
        } else {
            return null;
        }
        getNextToken();
        while (token.isType(TokenType.MULT) || token.isType(TokenType.DIV) || token.isType(TokenType.MOD)) {
            operators.add(token);
            unaryExp = tryParseUnaryExp();
            if (unaryExp != null) {
                unaryExps.add(unaryExp);
            } else {
                return null;
            }
            getNextToken();
        }
        retract(1);
        return new MulExp(unaryExps, operators);
    }

    private UnaryExp parseUnaryExp() {  // UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
        getNextToken();
        if (token.isType(TokenType.PLUS) || token.isType(TokenType.MINU) || token.isType(TokenType.NOT)) {
            retract(1);
            return new UnaryExp3(parseUnaryOp(), parseUnaryExp());
        } else if (token.isType(TokenType.INTCON) || token.isType(TokenType.CHRCON) || token.isType(TokenType.LPARENT)) {
            retract(1);
            return new UnaryExp1(parsePrimaryExp());
        } else if (token.isType(TokenType.IDENFR)) {
            Token ident = token;
            getNextToken();
            if (token.isType(TokenType.LPARENT)) {
                Token lParent = token;
                FuncRParams funcRParams = null;
                Token rParent = null;
                getNextToken();
                if (token.isType(TokenType.RPARENT)) {
                    rParent = token;
                } else if (token.isType(TokenType.LPARENT) || token.isType(TokenType.IDENFR)
                        || token.isType(TokenType.INTCON) || token.isType(TokenType.CHRCON)
                        || token.isType(TokenType.NOT) || token.isType(TokenType.PLUS) || token.isType(TokenType.MINU)) {
                    retract(1);
                    funcRParams = parseFuncRParams();
                    getNextToken();
                    if (token.isType(TokenType.RPARENT)) {
                        rParent = token;
                    } else {
                        retract(1);
                        errors.add(new Error(getLastTokenLineNumber(), 'j'));
                    }
                } else {
                    retract(1);
                    errors.add(new Error(getLastTokenLineNumber(), 'j'));
                }
                return new UnaryExp2(ident, lParent, funcRParams, rParent);
            } else {
                retract(2);
                return new UnaryExp1(parsePrimaryExp());
            }
        } else {
            System.err.println("Error at token " + token + ": expected IDENFR");
            return null;
        }
    }

    private UnaryExp tryParseUnaryExp() {
        getNextToken();
        if (token.isType(TokenType.PLUS) || token.isType(TokenType.MINU) || token.isType(TokenType.NOT)) {
            retract(1);
            UnaryOp unaryOp = parseUnaryOp();
            UnaryExp unaryExp = tryParseUnaryExp();
            if (unaryExp != null) {
                return new UnaryExp3(unaryOp, unaryExp);
            } else {
                return null;
            }
        } else if (token.isType(TokenType.INTCON) || token.isType(TokenType.CHRCON) || token.isType(TokenType.LPARENT)) {
            retract(1);
            PrimaryExp primaryExp = tryParsePrimaryExp();
            if (primaryExp != null) {
                return new UnaryExp1(primaryExp);
            } else {
                return null;
            }
        } else if (token.isType(TokenType.IDENFR)) {
            Token ident = token;
            getNextToken();
            if (token.isType(TokenType.LPARENT)) {
                Token lParent = token;
                FuncRParams funcRParams = null;
                Token rParent = null;
                getNextToken();
                if (token.isType(TokenType.RPARENT)) {
                    rParent = token;
                } else if (token.isType(TokenType.LPARENT) || token.isType(TokenType.IDENFR)
                        || token.isType(TokenType.INTCON) || token.isType(TokenType.CHRCON)
                        || token.isType(TokenType.NOT) || token.isType(TokenType.PLUS) || token.isType(TokenType.MINU)) {
                    retract(1);
                    funcRParams = tryParseFuncRParams();
                    getNextToken();
                    if (token.isType(TokenType.RPARENT)) {
                        rParent = token;
                    } else {
                        retract(1);
                    }
                } else {
                    retract(1);
                }
                return new UnaryExp2(ident, lParent, funcRParams, rParent);
            } else {
                retract(2);
                PrimaryExp primaryExp = tryParsePrimaryExp();
                if (primaryExp != null) {
                    return new UnaryExp1(primaryExp);
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    private PrimaryExp parsePrimaryExp() {  // PrimaryExp → '(' Exp ')' | LVal | Number | Character
        getNextToken();
        if (token.isType(TokenType.LPARENT)) {
            Token lParent = token;
            Exp exp = parseExp();
            Token rParent = null;
            getNextToken();
            if (token.isType(TokenType.RPARENT)) {
                rParent = token;
            } else {
                errors.add(new Error(getLastTokenLineNumber(), 'j'));
                retract(1);
            }
            return new PrimaryExp1(lParent, exp, rParent);
        } else if (token.isType(TokenType.IDENFR)) {
            retract(1);
            return new PrimaryExp2(parseLVal());
        } else if (token.isType(TokenType.INTCON)) {
            retract(1);
            return new PrimaryExp3(parseNumber());
        } else if (token.isType(TokenType.CHRCON)) {
            retract(1);
            return new PrimaryExp4(parseCharacter());
        } else {
            System.err.println("Error at token " + token + ": expected LPARENT or IDENFR or INTCON or CHRCON");
            return null;
        }
    }

    private PrimaryExp tryParsePrimaryExp() {
        getNextToken();
        if (token.isType(TokenType.LPARENT)) {
            Token lParent = token;
            Exp exp = tryParseExp();
            if (exp == null) {
                return null;
            }
            Token rParent = null;
            getNextToken();
            if (token.isType(TokenType.RPARENT)) {
                rParent = token;
            } else {
                retract(1);
            }
            return new PrimaryExp1(lParent, exp, rParent);
        } else if (token.isType(TokenType.IDENFR)) {
            retract(1);
            LVal lVal = tryParseLVal();
            if (lVal != null) {
                return new PrimaryExp2(lVal);
            } else {
                return null;
            }
        } else if (token.isType(TokenType.INTCON)) {
            retract(1);
            return new PrimaryExp3(parseNumber());
        } else if (token.isType(TokenType.CHRCON)) {
            retract(1);
            return new PrimaryExp4(parseCharacter());
        } else {
            return null;
        }
    }

    private Exp parseExp() {  // Exp → AddExp
        return new Exp(parseAddExp());
    }

    private Exp tryParseExp() {
        AddExp addExp = tryParseAddExp();
        if (addExp != null) {
            return new Exp(addExp);
        } else {
            return null;
        }
    }

    private LVal parseLVal() {  // LVal → Ident ['[' Exp ']']
        getNextToken();
        if (token.isType(TokenType.IDENFR)) {
            Token ident = token;
            Token lBrack = null;
            Exp exp = null;
            Token rBrack = null;
            getNextToken();
            if (token.isType(TokenType.LBRACK)) {
                lBrack = token;
                exp = parseExp();
                getNextToken();
                if (token.isType(TokenType.RBRACK)) {
                    rBrack = token;
                } else {
                    errors.add(new Error(getLastTokenLineNumber(), 'k'));
                    retract(1);
                }
            } else {
                retract(1);
            }
            return new LVal(ident, lBrack, exp, rBrack);
        } else {
            System.err.println("Error at token " + token + ": expected IDENFR");
            return null;
        }
    }

    private LVal tryParseLVal() {
        getNextToken();
        if (token.isType(TokenType.IDENFR)) {
            Token ident = token;
            Token lBrack = null;
            Exp exp = null;
            Token rBrack = null;
            getNextToken();
            if (token.isType(TokenType.LBRACK)) {
                lBrack = token;
                exp = tryParseExp();
                if (exp == null) {
                    return null;
                }
                getNextToken();
                if (token.isType(TokenType.RBRACK)) {
                    rBrack = token;
                } else {
                    retract(1);
                }
            } else {
                retract(1);
            }
            return new LVal(ident, lBrack, exp, rBrack);
        } else {
            return null;
        }
    }

    private Number parseNumber() {  // Number → IntConst
        getNextToken();
        if (token.isType(TokenType.INTCON)) {
            return new Number(token);
        } else {
            System.err.println("Error at token " + token + ": expected INTCON");
            return null;
        }
    }

    private Character parseCharacter() {  // Character → CharConst
        getNextToken();
        if (token.isType(TokenType.CHRCON)) {
            return new Character(token);
        } else {
            System.err.println("Error at token " + token + ": expected CHRCON");
            return null;
        }
    }

    private FuncRParams parseFuncRParams() {  // FuncRParams → Exp { ',' Exp }
        ArrayList<Exp> exps = new ArrayList<>();
        ArrayList<Token> commas = new ArrayList<>();
        exps.add(parseExp());
        getNextToken();
        while (token.isType(TokenType.COMMA)) {
            commas.add(token);
            exps.add(parseExp());
            getNextToken();
        }
        retract(1);
        return new FuncRParams(exps, commas);
    }

    private FuncRParams tryParseFuncRParams() {  // FuncRParams → Exp { ',' Exp }
        ArrayList<Exp> exps = new ArrayList<>();
        ArrayList<Token> commas = new ArrayList<>();
        Exp exp = tryParseExp();
        if (exp != null) {
            exps.add(exp);
        } else {
            return null;
        }
        getNextToken();
        while (token.isType(TokenType.COMMA)) {
            commas.add(token);
            exp = tryParseExp();
            if (exp != null) {
                exps.add(exp);
            } else {
                return null;
            }
            getNextToken();
        }
        retract(1);
        return new FuncRParams(exps, commas);
    }

    private UnaryOp parseUnaryOp() {  // UnaryOp → '+' | '−' | '!'
        getNextToken();
        if (token.isType(TokenType.PLUS) || token.isType(TokenType.MINU) || token.isType(TokenType.NOT)) {
            return new UnaryOp(token);
        } else {
            System.err.println("Error at token " + token + ": expected PLUS or MINU or NOT");
            return null;
        }
    }

    private ConstInitVal parseConstInitVal() {  // ConstInitVal → ConstExp | '{' [ ConstExp { ',' ConstExp } ] '}' | StringConst
        getNextToken();
        if (token.isType(TokenType.LBRACE)) {
            Token lBrace = token;
            ArrayList<ConstExp> constExps = new ArrayList<>();
            ArrayList<Token> commas = new ArrayList<>();
            Token rBrace;
            getNextToken();
            if (token.isType(TokenType.RBRACE)) {
                rBrace = token;
            } else {
                retract(1);
                constExps.add(parseConstExp());
                getNextToken();
                while (token.isType(TokenType.COMMA)) {
                    commas.add(token);
                    constExps.add(parseConstExp());
                    getNextToken();
                }
                if (token.isType(TokenType.RBRACE)) {
                    rBrace = token;
                } else {
                    System.err.println("Error at token " + token + ": expected RBRACE");
                    return null;
                }
            }
            return new ConstInitVal2(lBrace, constExps, commas, rBrace);
        } else if (token.isType(TokenType.STRCON)) {
            return new ConstInitVal3(token);
        } else {
            retract(1);
            return new ConstInitVal1(parseConstExp());
        }
    }

    private VarDecl parseVarDecl() {  // VarDecl → BType VarDef { ',' VarDef } ';'
        BType bType = parseBType();
        ArrayList<VarDef> varDefs = new ArrayList<>();
        ArrayList<Token> commas = new ArrayList<>();
        varDefs.add(parseVarDef());
        getNextToken();
        while (token.isType(TokenType.COMMA)) {
            commas.add(token);
            varDefs.add(parseVarDef());
            getNextToken();
        }
        Token semicn = null;
        if (token.isType(TokenType.SEMICN)) {
            semicn = token;
        } else {
            errors.add(new Error(getLastTokenLineNumber(), 'i'));
            retract(1);
        }
        return new VarDecl(bType, varDefs, commas, semicn);
    }

    private VarDef parseVarDef() {  // VarDef → Ident [ '[' ConstExp ']' ] | Ident [ '[' ConstExp ']' ] '=' InitVal
        getNextToken();
        if (token.isType(TokenType.IDENFR)) {
            Token ident = token;
            Token lBrack = null;
            ConstExp constExp = null;
            Token rBrack = null;
            getNextToken();
            if (token.isType(TokenType.LBRACK)) {
                lBrack = token;
                constExp = parseConstExp();
                getNextToken();
                if (token.isType(TokenType.RBRACK)) {
                    rBrack = token;
                    getNextToken();
                } else {
                    errors.add(new Error(getLastTokenLineNumber(), 'k'));
                }
            }
            Token assign = null;
            InitVal initVal = null;
            if (token.isType(TokenType.ASSIGN)) {
                assign = token;
                initVal = parseInitVal();
            } else {
                retract(1);
            }
            return new VarDef(ident, lBrack, constExp, rBrack, assign, initVal);
        } else {
            System.err.println("Error at token " + token + ": expected IDENFR");
            return null;
        }
    }

    private InitVal parseInitVal() {  // InitVal → Exp | '{' [ Exp { ',' Exp } ] '}' | StringConst
        getNextToken();
        if (token.isType(TokenType.LBRACE)) {
            Token lBrace = token;
            ArrayList<Exp> exps = new ArrayList<>();
            ArrayList<Token> commas = new ArrayList<>();
            Token rBrace;
            getNextToken();
            if (token.isType(TokenType.RBRACE)) {
                rBrace = token;
            } else {
                retract(1);
                exps.add(parseExp());
                getNextToken();
                while (token.isType(TokenType.COMMA)) {
                    commas.add(token);
                    exps.add(parseExp());
                    getNextToken();
                }
                if (token.isType(TokenType.RBRACE)) {
                    rBrace = token;
                } else {
                    System.err.println("Error at token " + token + ": expected RBRACE");
                    return null;
                }
            }
            return new InitVal2(lBrace, exps, commas, rBrace);
        } else if (token.isType(TokenType.STRCON)) {
            return new InitVal3(token);
        } else {
            retract(1);
            return new InitVal1(parseExp());
        }
    }

    private FuncDef parseFuncDef() {  // FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
        FuncType funcType = parseFuncType();
        getNextToken();
        if (token.isType(TokenType.IDENFR)) {
            Token ident = token;
            getNextToken();
            if (token.isType(TokenType.LPARENT)) {
                Token lParent = token;
                FuncFParams funcFParams = null;
                Token rParent = null;
                getNextToken();
                if (token.isType(TokenType.RPARENT)) {
                    rParent = token;
                } else if (token.isType(TokenType.LBRACE)) {
                    errors.add(new Error(getLastTokenLineNumber(), 'j'));
                    retract(1);
                } else {
                    retract(1);
                    funcFParams = parseFuncFParams();
                    getNextToken();
                    if (token.isType(TokenType.RPARENT)) {
                        rParent = token;
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'j'));
                        retract(1);
                    }
                }
                return new FuncDef(funcType, ident, lParent, funcFParams, rParent, parseBlock());
            } else {
                System.err.println("Error at token " + token + ": expected LBRACE");
                return null;
            }
        } else {
            System.err.println("Error at token " + token + ": expected IDENFR");
            return null;
        }
    }

    private FuncType parseFuncType() {  // FuncType → 'void' | 'int' | 'char'
        getNextToken();
        if (token.isType(TokenType.VOIDTK) || token.isType(TokenType.INTTK) || token.isType(TokenType.CHARTK)) {
            return new FuncType(token);
        } else {
            System.err.println("Error at token " + token + ": expected VOIDTK or INTTK or CHARTK");
            return null;
        }
    }

    private FuncFParams parseFuncFParams() {  // FuncFParams → FuncFParam { ',' FuncFParam }
        ArrayList<FuncFParam> funcFParams = new ArrayList<>();
        ArrayList<Token> commas = new ArrayList<>();
        funcFParams.add(parseFuncFParam());
        getNextToken();
        while (token.isType(TokenType.COMMA)) {
            commas.add(token);
            funcFParams.add(parseFuncFParam());
            getNextToken();
        }
        retract(1);
        return new FuncFParams(funcFParams, commas);
    }

    private FuncFParam parseFuncFParam() {  // FuncFParam → BType Ident ['[' ']']
        BType bType = parseBType();
        getNextToken();
        if (token.isType(TokenType.IDENFR)) {
            Token ident = token;
            Token lBrack = null;
            Token rBrack = null;
            getNextToken();
            if (token.isType(TokenType.LBRACK)) {
                lBrack = token;
                getNextToken();
                if (token.isType(TokenType.RBRACK)) {
                    rBrack = token;
                } else {
                    errors.add(new Error(getLastTokenLineNumber(), 'k'));
                    retract(1);
                }
            } else {
                retract(1);
            }
            return new FuncFParam(bType, ident, lBrack, rBrack);
        } else {
            System.err.println("Error at token " + token + ": expected IDENFR");
            return null;
        }
    }

    private Block parseBlock() {  // Block → '{' { BlockItem } '}'
        getNextToken();
        if (token.isType(TokenType.LBRACE)) {
            Token lBrace = token;
            ArrayList<BlockItem> blockItems = new ArrayList<>();
            getNextToken();
            while (!token.isType(TokenType.RBRACE) && !token.isType(TokenType.EOF)) {
                retract(1);
                blockItems.add(parseBlockItem());
                getNextToken();
            }
            if (token.isType(TokenType.RBRACE)) {
                return new Block(lBrace, blockItems, token);
            } else {
                System.err.println("Error at token " + token + ": expected RBRACE");
                return null;
            }
        } else {
            System.err.println("Error at token " + token + ": expected LBRACE");
            return null;
        }
    }

    private BlockItem parseBlockItem() {  // BlockItem → Decl | Stmt
        getNextToken();
        if (token.isType(TokenType.CONSTTK) || token.isType(TokenType.INTTK) || token.isType(TokenType.CHARTK)) {
            retract(1);
            return new BlockItem1(parseDecl());
        } else {
            retract(1);
            return new BlockItem2(parseStmt());
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
    private Stmt parseStmt() {
        getNextToken();
        if (token.isType(TokenType.LBRACE)) {
            retract(1);
            return new Stmt3(parseBlock());
        } else if (token.isType(TokenType.IFTK)) {
            Token ifTk = token;
            getNextToken();
            if (token.isType(TokenType.LPARENT)) {
                Token lParent = token;
                Cond cond = parseCond();
                Token rParent = null;
                getNextToken();
                if (token.isType(TokenType.RPARENT)) {
                    rParent = token;
                } else {
                    errors.add(new Error(getLastTokenLineNumber(), 'j'));
                    retract(1);
                }
                Stmt stmt = parseStmt();
                Token elseTk = null;
                Stmt elseStmt = null;
                getNextToken();
                if (token.isType(TokenType.ELSETK)) {
                    elseTk = token;
                    elseStmt = parseStmt();
                } else {
                    retract(1);
                }
                return new Stmt4(ifTk, lParent, cond, rParent, stmt, elseTk, elseStmt);
            } else {
                System.err.println("Error at token " + token + ": expected LPARENT");
                return null;
            }
        } else if (token.isType(TokenType.FORTK)) {
            Token forTk = token;
            getNextToken();
            if (token.isType(TokenType.LPARENT)) {
                Token lParent = token;
                ForStmt forStmt1 = null;
                Token semicn1;
                getNextToken();
                if (token.isType(TokenType.SEMICN)) {
                    semicn1 = token;
                } else {
                    retract(1);
                    forStmt1 = parseForStmt();
                    getNextToken();
                    if (token.isType(TokenType.SEMICN)) {
                        semicn1 = token;
                    } else {
                        System.err.println("Error at token " + token + ": expected SEMICN");
                        return null;
                    }
                }
                Cond cond = null;
                Token semicn2;
                getNextToken();
                if (token.isType(TokenType.SEMICN)) {
                    semicn2 = token;
                } else {
                    retract(1);
                    cond = parseCond();
                    getNextToken();
                    if (token.isType(TokenType.SEMICN)) {
                        semicn2 = token;
                    } else {
                        System.err.println("Error at token " + token + ": expected SEMICN");
                        return null;
                    }
                }
                ForStmt forStmt2 = null;
                Token rParent = null;
                getNextToken();
                if (token.isType(TokenType.RPARENT)) {
                    rParent = token;
                } else {
                    retract(1);
                    forStmt2 = parseForStmt();
                    getNextToken();
                    if (token.isType(TokenType.RPARENT)) {
                        rParent = token;
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'j'));
                        retract(1);
                    }
                }
                return new Stmt5(forTk, lParent, forStmt1, semicn1, cond, semicn2, forStmt2, rParent, parseStmt());
            } else {
                System.err.println("Error at token " + token + ": expected LPARENT");
                return null;
            }
        } else if (token.isType(TokenType.BREAKTK)) {
            Token breakTk = token;
            Token semicn = null;
            getNextToken();
            if (token.isType(TokenType.SEMICN)) {
                semicn = token;
            } else {
                errors.add(new Error(getLastTokenLineNumber(), 'i'));
                retract(1);
            }
            return new Stmt6(breakTk, semicn);
        } else if (token.isType(TokenType.CONTINUETK)) {
            Token continueTk = token;
            Token semicn = null;
            getNextToken();
            if (token.isType(TokenType.SEMICN)) {
                semicn = token;
            } else {
                errors.add(new Error(getLastTokenLineNumber(), 'i'));
                retract(1);
            }
            return new Stmt7(continueTk, semicn);
        } else if (token.isType(TokenType.RETURNTK)) {
            Token returnTk = token;
            Exp exp = null;
            Token semicn = null;
            getNextToken();
            if (token.isType(TokenType.SEMICN)) {
                semicn = token;
            } else {
                retract(1);
                int oriIndex = index;
                exp = tryParseExp();
                if (exp == null) {
                    setIndex(oriIndex);
                    errors.add(new Error(tokens.get(index - 1).getLineNumber(), 'i'));
                } else {
                    getNextToken();
                    if (token.isType(TokenType.ASSIGN)) {
                        exp = null;
                        setIndex(oriIndex);
                        errors.add(new Error(tokens.get(index - 1).getLineNumber(), 'i'));
                    } else {
                        setIndex(oriIndex);
                        exp = parseExp();
                        getNextToken();
                        if (token.isType(TokenType.SEMICN)) {
                            semicn = token;
                        } else {
                            errors.add(new Error(getLastTokenLineNumber(), 'i'));
                            retract(1);
                        }
                    }
                }
            }
            return new Stmt8(returnTk, exp, semicn);
        } else if (token.isType(TokenType.PRINTFTK)) {
            Token printf = token;
            getNextToken();
            if (token.isType(TokenType.LPARENT)) {
                Token lParent = token;
                getNextToken();
                if (token.isType(TokenType.STRCON)) {
                    Token stringConst = token;
                    ArrayList<Exp> exps = new ArrayList<>();
                    ArrayList<Token> commas = new ArrayList<>();
                    getNextToken();
                    while (token.isType(TokenType.COMMA)) {
                        commas.add(token);
                        exps.add(parseExp());
                        getNextToken();
                    }
                    Token rParent = null;
                    if (token.isType(TokenType.RPARENT)) {
                        rParent = token;
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'j'));
                        retract(1);
                    }
                    Token semicn = null;
                    getNextToken();
                    if (token.isType(TokenType.SEMICN)) {
                        semicn = token;
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'i'));
                        retract(1);
                    }
                    return new Stmt11(printf, lParent, stringConst, exps, commas, rParent, semicn);
                } else {
                    System.err.println("Error at token " + token + ": expected STRCON");
                    return null;
                }
            } else {
                System.err.println("Error at token " + token + ": expected LPARENT");
                return null;
            }
        } else if (token.isType(TokenType.SEMICN)) {
            return new Stmt2(null, token);
        } else if (token.isType(TokenType.IDENFR)) {
            retract(1);
            int oriIndex = index;
            tryParseLVal();
            getNextToken();
            if (token.isType(TokenType.ASSIGN)) {
                setIndex(oriIndex);
                LVal lVal = parseLVal();
                getNextToken();
                Token assign = token;
                getNextToken();
                if (token.isType(TokenType.GETINTTK)) {
                    Token getint = token;
                    Token lParent;
                    Token rParent = null;
                    getNextToken();
                    if (token.isType(TokenType.LPARENT)) {
                        lParent = token;
                        getNextToken();
                        if (token.isType(TokenType.RPARENT)) {
                            rParent = token;
                        } else {
                            errors.add(new Error(getLastTokenLineNumber(), 'j'));
                            retract(1);
                        }
                    } else {
                        System.err.println("Error at token " + token + ": expected LPARENT");
                        return null;
                    }
                    Token semicn = null;
                    getNextToken();
                    if (token.isType(TokenType.SEMICN)) {
                        semicn = token;
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'i'));
                        retract(1);
                    }
                    return new Stmt9(lVal, assign, getint, lParent, rParent, semicn);
                } else if (token.isType(TokenType.GETCHARTK)) {
                    Token getchar = token;
                    Token lParent;
                    Token rParent = null;
                    getNextToken();
                    if (token.isType(TokenType.LPARENT)) {
                        lParent = token;
                        getNextToken();
                        if (token.isType(TokenType.RPARENT)) {
                            rParent = token;
                        } else {
                            errors.add(new Error(getLastTokenLineNumber(), 'j'));
                            retract(1);
                        }
                    } else {
                        System.err.println("Error at token " + token + ": expected LPARENT");
                        return null;
                    }
                    Token semicn = null;
                    getNextToken();
                    if (token.isType(TokenType.SEMICN)) {
                        semicn = token;
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'i'));
                        retract(1);
                    }
                    return new Stmt10(lVal, assign, getchar, lParent, rParent, semicn);
                } else {
                    retract(1);
                    Exp exp = parseExp();
                    Token semicn = null;
                    getNextToken();
                    if (token.isType(TokenType.SEMICN)) {
                        semicn = token;
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'i'));
                        retract(1);
                    }
                    return new Stmt1(lVal, assign, exp, semicn);
                }
            } else {
                setIndex(oriIndex);
                Exp exp = parseExp();
                Token semicn = null;
                getNextToken();
                if (token.isType(TokenType.SEMICN)) {
                    semicn = token;
                } else {
                    errors.add(new Error(getLastTokenLineNumber(), 'i'));
                    retract(1);
                }
                return new Stmt2(exp, semicn);
            }
        } else {
            retract(1);
            Exp exp = parseExp();
            Token semicn = null;
            getNextToken();
            if (token.isType(TokenType.SEMICN)) {
                semicn = token;
            } else {
                errors.add(new Error(getLastTokenLineNumber(), 'i'));
                retract(1);
            }
            return new Stmt2(exp, semicn);
        }
    }

    private Cond parseCond() {  // Cond → LOrExp
        return new Cond(parseLOrExp());
    }

    private LOrExp parseLOrExp() {  // LOrExp → LAndExp | LOrExp '||' LAndExp
        ArrayList<LAndExp> lAndExps = new ArrayList<>();
        ArrayList<Token> operators = new ArrayList<>();
        lAndExps.add(parseLAndExp());
        getNextToken();
        while (token.isType(TokenType.OR)) {
            operators.add(token);
            lAndExps.add(parseLAndExp());
            getNextToken();
        }
        retract(1);
        return new LOrExp(lAndExps, operators);
    }

    private LAndExp parseLAndExp() {  // LAndExp → EqExp | LAndExp '&&' EqExp
        ArrayList<EqExp> eqExps = new ArrayList<>();
        ArrayList<Token> operators = new ArrayList<>();
        eqExps.add(parseEqExp());
        getNextToken();
        while (token.isType(TokenType.AND)) {
            operators.add(token);
            eqExps.add(parseEqExp());
            getNextToken();
        }
        retract(1);
        return new LAndExp(eqExps, operators);
    }

    private EqExp parseEqExp() {  // EqExp → RelExp | EqExp ('==' | '!=') RelExp
        ArrayList<RelExp> relExps = new ArrayList<>();
        ArrayList<Token> operators = new ArrayList<>();
        relExps.add(parseRelExp());
        getNextToken();
        while (token.isType(TokenType.EQL) || token.isType(TokenType.NEQ)) {
            operators.add(token);
            relExps.add(parseRelExp());
            getNextToken();
        }
        retract(1);
        return new EqExp(relExps, operators);
    }

    private RelExp parseRelExp() {  // RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
        ArrayList<AddExp> addExps = new ArrayList<>();
        ArrayList<Token> operators = new ArrayList<>();
        addExps.add(parseAddExp());
        getNextToken();
        while (token.isType(TokenType.LSS) || token.isType(TokenType.GRE) || token.isType(TokenType.LEQ) || token.isType(TokenType.GEQ)) {
            operators.add(token);
            addExps.add(parseAddExp());
            getNextToken();
        }
        retract(1);
        return new RelExp(addExps, operators);
    }

    private ForStmt parseForStmt() {  // ForStmt → LVal '=' Exp
        LVal lVal = parseLVal();
        getNextToken();
        if (token.isType(TokenType.ASSIGN)) {
            return new ForStmt(lVal, token, parseExp());
        } else {
            System.err.println("Error at token " + token + ": expected ASSIGN");
            return null;
        }
    }

    private MainFuncDef parseMainFuncDef() {  // MainFuncDef → 'int' 'main' '(' ')' Block
        getNextToken();
        if (token.isType(TokenType.INTTK)) {
            Token intTk = token;
            getNextToken();
            if (token.isType(TokenType.MAINTK)) {
                Token main = token;
                getNextToken();
                if (token.isType(TokenType.LPARENT)) {
                    Token lParent = token;
                    Token rParent = null;
                    getNextToken();
                    if (token.isType(TokenType.RPARENT)) {
                        rParent = token;
                    } else {
                        errors.add(new Error(getLastTokenLineNumber(), 'j'));
                        retract(1);
                    }
                    return new MainFuncDef(intTk, main, lParent, rParent, parseBlock());
                } else {
                    System.err.println("Error at token " + token + ": expected LPARENT");
                    return null;
                }
            } else {
                System.err.println("Error at token " + token + ": expected MAINTK");
                return null;
            }
        } else {
            System.err.println("Error at token " + token + ": expected INTTK");
            return null;
        }
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
}
