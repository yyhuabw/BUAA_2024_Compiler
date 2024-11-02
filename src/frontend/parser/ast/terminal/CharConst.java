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
     */
    @Override
    public IrValue genIR() {
        int value = token.getContent().charAt(0);
        return new IrConstInt(IrIntType.INT8, value);
    }
}
