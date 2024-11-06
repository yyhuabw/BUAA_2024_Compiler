package frontend.parser.ast.declaration.type;

import frontend.lexer.token.Token;
import frontend.parser.ast.function.funcType.FuncTypeEle;
import middle.llvm_ir.IrValue;

public class IntType implements BTypeEle, FuncTypeEle {
    private final Token intTk;

    public IntType(Token intTk) {
        this.intTk = intTk;
    }

    @Override
    public String syntaxInfoOutput() {
        return intTk.syntaxInfoOutput();
    }

    /**
     * not use
     * @return null
     */
    @Override
    public IrValue genIR() {
        return null;
    }
}
