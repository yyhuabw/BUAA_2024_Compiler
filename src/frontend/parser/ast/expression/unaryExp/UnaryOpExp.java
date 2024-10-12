package frontend.parser.ast.expression.unaryExp;

import middle.symbol.value.ValueType;

public class UnaryOpExp implements UnaryExpEle {
    private final UnaryOp unaryOp;
    private final UnaryExp unaryExp;

    public UnaryOpExp(UnaryOp unaryOp, UnaryExp unaryExp) {
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    @Override
    public ValueType getValueType() {
        return unaryExp.getValueType();
    }

    @Override
    public int getDim() {
        return unaryExp.getDim();
    }

    @Override
    public String syntaxInfoOutput() {
        return unaryOp.syntaxInfoOutput() + unaryExp.syntaxInfoOutput();
    }
}
