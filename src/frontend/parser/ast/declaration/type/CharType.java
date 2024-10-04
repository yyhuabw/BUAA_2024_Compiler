package frontend.parser.ast.declaration.type;

import frontend.lexer.token.Token;
import frontend.parser.ast.function.funcType.FuncTypeEle;

public class CharType implements BTypeEle, FuncTypeEle {
    private final Token charTk;

    public CharType(Token charTk) {
        this.charTk = charTk;
    }

    @Override
    public String syntaxInfoOutput() {
        return charTk.syntaxInfoOutput();
    }
}
