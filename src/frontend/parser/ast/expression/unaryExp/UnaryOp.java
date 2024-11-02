package frontend.parser.ast.expression.unaryExp;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import middle.llvm_ir.IrValue;

public class UnaryOp implements SyntaxNode {
    private final SyntaxType type;
    // '+' | '−' | '!'
    private final Token token;

    public UnaryOp(Token token) {
        this.type = SyntaxType.UNARY_OP;
        this.token = token;
    }

    public TokenType getOpType() {
        return token.getType();
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput() + type.getName() + "\n";
    }

    /**
     * not use
     * @return null
     */
    @Override
    public IrValue genIR() {
        return null;
    }
}
