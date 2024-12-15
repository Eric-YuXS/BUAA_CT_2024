package frontend;

public enum SymbolType {
    Bool,
    Int, IntArray, ConstInt, ConstIntArray,
    Char, CharArray, ConstChar, ConstCharArray,
    VoidFunc, IntFunc, CharFunc;

    public static SymbolType TokenToSymbolType(Token token) {
        if (token.isType(TokenType.INTTK)) {
            return Int;
        } else if (token.isType(TokenType.CHARTK)) {
            return Char;
        } else if (token.isType(TokenType.VOIDTK)) {
            return VoidFunc;
        } else {
            return null;
        }
    }

    public SymbolType varToArray() {
        switch (this) {
            case Int:
                return IntArray;
            case Char:
                return CharArray;
            default:
                System.err.println("Cannot change to Array: " + this);
                return null;
        }
    }

    public SymbolType varToConst() {
        switch (this) {
            case Int:
                return ConstInt;
            case Char:
                return ConstChar;
            default:
                System.err.println("Cannot change to Const: " + this);
                return null;
        }
    }

    public SymbolType constToConstArray() {
        switch (this) {
            case ConstInt:
                return ConstIntArray;
            case ConstChar:
                return ConstCharArray;
            default:
                System.err.println("Cannot change to ConstArray: " + this);
                return null;
        }
    }

    public SymbolType arrayOrVarToVar() {
        switch (this) {
            case Int:
            case ConstInt:
            case IntArray:
            case ConstIntArray:
                return Int;
            case Char:
            case ConstChar:
            case CharArray:
            case ConstCharArray:
                return Char;
            default:
                System.err.println("Cannot change to Var: " + this);
                return null;
        }
    }

    public SymbolType varOrVoidToFunc() {
        switch (this) {
            case Int:
                return IntFunc;
            case Char:
                return CharFunc;
            case VoidFunc:
                return VoidFunc;
            default:
                System.err.println("Cannot change to Func: " + this);
                return null;
        }
    }

    public SymbolType funcToVarOrVoid() {
        switch (this) {
            case IntFunc:
                return Int;
            case CharFunc:
                return Char;
            case VoidFunc:
                return VoidFunc;
            default:
                System.err.println("Cannot change to Var: " + this);
                return null;
        }
    }

    public boolean isConst() {
        switch (this) {
            case ConstInt:
            case ConstIntArray:
            case ConstChar:
            case ConstCharArray:
                return true;
            default:
                return false;
        }
    }

    public boolean isArray() {
        switch (this) {
            case IntArray:
            case ConstIntArray:
            case CharArray:
            case ConstCharArray:
                return true;
            default:
                return false;
        }
    }

    public boolean isVar() {
        switch (this) {
            case Int:
            case ConstInt:
            case Char:
            case ConstChar:
                return true;
            default:
                return false;
        }
    }

    public boolean isFunc() {
        switch (this) {
            case VoidFunc:
            case IntFunc:
            case CharFunc:
                return true;
            default:
                return false;
        }
    }

    public boolean canAcceptRParam(SymbolType symbolType) {
        switch (this) {
            case Int:
            case Char:
                return symbolType.isVar() || symbolType == IntFunc || symbolType == CharFunc;
            case IntArray:
                return symbolType == IntArray || symbolType == ConstIntArray;
            case CharArray:
                return symbolType == CharArray || symbolType == ConstCharArray;
            default:
                System.err.println("Invalid type: " + this);
                return false;
        }
    }

    public String toValueString() {
        switch (this) {
            case Int:
            case ConstInt:
            case IntFunc:
                return "i32";
            case Char:
            case ConstChar:
            case CharFunc:
                return "i8";
            case IntArray:
            case ConstIntArray:
                return "i32*";
            case CharArray:
            case ConstCharArray:
                return "i8*";
            case VoidFunc:
                return "void";
            case Bool:
                return "i1";
            default:
                return null;
        }
    }

    public boolean isI1() {
        return this == SymbolType.Bool;
    }

    public boolean isI8() {
        switch (this) {
            case Char:
            case ConstChar:
            case CharFunc:
                return true;
            default:
                return false;
        }
    }

    public boolean isI32() {
        switch (this) {
            case Int:
            case ConstInt:
            case IntFunc:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case Int:
                return "Int";
            case IntArray:
                return "IntArray";
            case ConstInt:
                return "ConstInt";
            case ConstIntArray:
                return "ConstIntArray";
            case Char:
                return "Char";
            case CharArray:
                return "CharArray";
            case ConstChar:
                return "ConstChar";
            case ConstCharArray:
                return "ConstCharArray";
            case VoidFunc:
                return "VoidFunc";
            case IntFunc:
                return "IntFunc";
            case CharFunc:
                return "CharFunc";
            default:
                return "";
        }
    }
}
