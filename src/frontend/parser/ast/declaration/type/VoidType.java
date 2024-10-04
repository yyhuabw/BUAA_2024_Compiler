package frontend.parser.ast.declaration.type;

import frontend.lexer.token.Token;
import frontend.parser.ast.function.funcType.FuncTypeEle;

public class VoidType implements FuncTypeEle {
    private final Token voidTk;

    public VoidType(Token voidTk) {
        this.voidTk = voidTk;
    }

    @Override
    public String syntaxInfoOutput() {
        return voidTk.syntaxInfoOutput();
    }
}
