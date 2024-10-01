package frontend.parser.ast.terminal;

import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.ast.SyntaxNode;

public class CharConst implements SyntaxNode {
    private final Token token;

    public CharConst(Token token) {
        this.token = token;
    }

    public CharConst(String content, int lineno) {
        this.token = new Token(TokenType.CHRCON, content, lineno);
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput();
    }
}
