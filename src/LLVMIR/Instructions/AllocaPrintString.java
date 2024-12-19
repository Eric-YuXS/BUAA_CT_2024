package LLVMIR.Instructions;

import LLVMIR.Instruction;
import LLVMIR.Module;
import LLVMIR.ValueType;
import frontend.SymbolType;

import java.util.ArrayList;

public class AllocaPrintString extends Instruction {
    private String preparedString;
    private final String string;  // without quotations
    private int size;

    public AllocaPrintString(Module module, String string) {
        super(null, module.getNextStringInstructionName(), ValueType.POINTER, SymbolType.ConstCharArray, new ArrayList<>());
        this.string = string;
        prepareString(string);
    }

    public int getSize() {
        return size;
    }

    private void prepareString(String string) {
        StringBuilder sb = new StringBuilder();
        int newLength = 0;
        char[] chars = string.toCharArray();
        int i = 0;
        while (i < chars.length) {
            char c = chars[i];
            if (c == '\\' && i + 1 < chars.length) {
                char next = chars[i + 1];
                switch (next) {
                    case 'a':
                        sb.append("\\07");
                        newLength++;
                        break;
                    case 'b':
                        sb.append("\\08");
                        newLength++;
                        break;
                    case 't':
                        sb.append("\\09");
                        newLength++;
                        break;
                    case 'n':
                        sb.append("\\0A");
                        newLength++;
                        break;
                    case 'v':
                        sb.append("\\0B");
                        newLength++;
                        break;
                    case 'f':
                        sb.append("\\0C");
                        newLength++;
                        break;
                    case 'r':
                        sb.append("\\0D");
                        newLength++;
                        break;
                    case '0':
                        sb.append("\\00");
                        newLength++;
                        break;
                    case '\\':
                        sb.append("\\5C");
                        newLength++;
                        break;
                    case '"':
                        sb.append("\\22");
                        newLength++;
                        break;
                    case '\'':
                        sb.append("\\27");
                        newLength++;
                        break;
                    default:
                        sb.append(next);
                        newLength += 1;
                        break;
                }
                i += 2;
            } else if (c >= 32 && c <= 126) {
                sb.append(c);
                newLength++;
                i++;
            } else {
                sb.append(String.format("\\%02X", (int) c));
                newLength++;
                i++;
            }
        }
        sb.append("\\00");
        newLength++;
        this.preparedString = sb.toString();
        size = newLength;
    }

    @Override
    public String toString() {
        return "@" + getName() + " = private unnamed_addr constant [" + size + " x i8] c\"" +
                preparedString + "\", align 1\n";
    }

    @Override
    public String toNameString() {
        return "@" + getName();
    }

    @Override
    public String toTypeAndNameString() {
        return "[" + size + " x i8]* @" + getName();
    }

    @Override
    public String toMips() {
        return getName() + ":\t.asciiz \"" + string + "\"\n";
    }
}
