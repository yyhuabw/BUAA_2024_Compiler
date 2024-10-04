package frontend.parser.ast.terminal;

import frontend.lexer.token.Token;
import frontend.parser.ast.declaration.constant.constInitVal.ConstInitValEle;
import frontend.parser.ast.declaration.variable.initVal.InitValEle;

public class StringConst implements ConstInitValEle, InitValEle {
    private final Token token;

    public StringConst(Token token) {
        this.token = token;
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput();
    }
}
