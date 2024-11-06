package frontend.parser.ast.declaration.type;

import frontend.parser.ast.SyntaxNode;
import middle.llvm_ir.IrValue;
import middle.symbol.value.ValueType;

public class BType implements SyntaxNode {
    private final BTypeEle bTypeEle;

    public BType(BTypeEle bTypeEle) {
        this.bTypeEle = bTypeEle;
    }

    public ValueType getValueType() { // for symbol
        if (bTypeEle instanceof CharType) {
            return ValueType.CHAR;
        } else if (bTypeEle instanceof IntType) {
            return ValueType.INT;
        }
        return null;
    }

    @Override
    public String syntaxInfoOutput() {
        return bTypeEle.syntaxInfoOutput();
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
