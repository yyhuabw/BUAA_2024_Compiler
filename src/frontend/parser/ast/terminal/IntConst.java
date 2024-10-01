package frontend.parser.ast.terminal;

import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.ast.SyntaxNode;

public class IntConst implements SyntaxNode {
    private final Token token;

    public IntConst(Token token) {
        this.token = token;
    }

    public IntConst(String content, int lineno) {
        this.token = new Token(TokenType.INTCON, content, lineno);
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput();
    }
}
