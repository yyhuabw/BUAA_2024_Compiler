package frontend.parser.ast.terminal;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.utils.constant.IrConstInt;

public class IntConst implements SyntaxNode {
    private final Token token;

    public IntConst(Token token) {
        this.token = token;
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput();
    }

    public int evaluate() {
        return Integer.parseInt(token.getContent());
    }

    @Override
    public IrValue genIR() {
        int value = Integer.parseInt(token.getContent());
        return new IrConstInt(IrIntType.INT32, value);
    }
}
