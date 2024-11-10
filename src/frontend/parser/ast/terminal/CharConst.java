package frontend.parser.ast.terminal;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.utils.constant.IrConstInt;

public class CharConst implements SyntaxNode {
    private final Token token;

    public CharConst(Token token) {
        this.token = token;
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput();
    }

    /**
     * the content string should only have one char
     * ''
     * 'c'
     * '\c'
     */
    public int evaluate() {
        String fixedContent = token.getContent().substring(1, token.getContent().length() - 1);
        if (fixedContent.isEmpty()) { // null
            return 0;
        }
        if (fixedContent.charAt(0) == '\\') {
            return switch (fixedContent.charAt(1)) {
                case 'a' -> 7;
                case 'b' -> 8;
                case 't' -> 9;
                case 'n' -> 10;
                case 'v' -> 11;
                case 'f' -> 12;
                case '\"' -> 34;
                case '\'' -> 39;
                case '\\' -> 92;
                case '\0' -> -1;

                default -> 0;
            };
        }
        return fixedContent.charAt(0);
    }

    /**
     * the content string should only have one char
     */
    @Override
    public IrValue genIR() {
        return new IrConstInt(IrIntType.INT8, evaluate());
    }
}
