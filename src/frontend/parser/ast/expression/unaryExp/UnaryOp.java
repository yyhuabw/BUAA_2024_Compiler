package frontend.parser.ast.expression.unaryExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;

public class UnaryOp implements SyntaxNode {
    private final SyntaxType type;
    private final Token token;

    public UnaryOp(Token token) {
        this.type = SyntaxType.UNARY_OP;
        this.token = token;
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput() + type.getName() + "\n";
    }
}
