package frontend.parser.ast.terminal;

import frontend.lexer.Token;
import frontend.parser.ast.SyntaxNode;

public class Ident implements SyntaxNode {
    private final Token token;

    public Ident(Token token) {
        this.token = token;
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput();
    }
}
