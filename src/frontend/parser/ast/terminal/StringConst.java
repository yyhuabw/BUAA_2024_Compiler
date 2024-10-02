package frontend.parser.ast.terminal;

import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.ast.declaration.constant.constInitVal.ConstInitValEle;
import frontend.parser.ast.declaration.variable.initVal.InitValEle;

public class StringConst implements ConstInitValEle, InitValEle {
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
