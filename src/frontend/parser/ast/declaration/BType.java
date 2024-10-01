package frontend.parser.ast.declaration;

import frontend.lexer.Token;
import frontend.parser.ast.SyntaxNode;

public class BType implements SyntaxNode {
    private final Token token;

    public BType(Token token) {
        this.token = token;
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput();
    }
}
