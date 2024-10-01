package frontend.parser.ast.terminal;

import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.ast.SyntaxNode;

public class Ident implements SyntaxNode {
    private final Token token;

    public Ident(Token token) {
        this.token = token;
    }

    public Ident(String content, int lineno) {
        this.token = new Token(TokenType.IDENFR, content, lineno);
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput();
    }
}
