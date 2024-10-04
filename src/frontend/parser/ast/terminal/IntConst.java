package frontend.parser.ast.terminal;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;

public class IntConst implements SyntaxNode {
    private final Token token;

    public IntConst(Token token) {
        this.token = token;
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput();
    }
}
