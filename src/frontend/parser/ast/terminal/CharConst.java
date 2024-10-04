package frontend.parser.ast.terminal;

import frontend.lexer.Token;
import frontend.parser.ast.SyntaxNode;

public class CharConst implements SyntaxNode {
    private final Token token;

    public CharConst(Token token) {
        this.token = token;
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput();
    }
}
