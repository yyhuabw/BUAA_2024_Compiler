package frontend.parser.ast.declaration.type;

import frontend.lexer.token.Token;
import frontend.parser.ast.function.funcType.FuncTypeEle;
import middle.llvm_ir.IrValue;

public class VoidType implements FuncTypeEle {
    private final Token voidTk;

    public VoidType(Token voidTk) {
        this.voidTk = voidTk;
    }

    @Override
    public String syntaxInfoOutput() {
        return voidTk.syntaxInfoOutput();
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
