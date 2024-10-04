package frontend.parser.ast.declaration.type;

import frontend.lexer.token.Token;
import frontend.parser.ast.function.funcType.FuncTypeEle;

public class IntType implements BTypeEle, FuncTypeEle {
    private final Token intTk;

    public IntType(Token intTk) {
        this.intTk = intTk;
    }

    @Override
    public String syntaxInfoOutput() {
        return intTk.syntaxInfoOutput();
    }
}
