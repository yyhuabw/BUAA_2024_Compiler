package frontend.parser.ast.terminal;

import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.declaration.constant.constInitVal.ConstInitValEle;

public class StringConst implements ConstInitValEle {
    private final Token token;

    public StringConst(Token token) {
        this.token = token;
    }

    public StringConst(String content, int lineno) {
        this.token = new Token(TokenType.STRCON, content, lineno);
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput();
    }
}
