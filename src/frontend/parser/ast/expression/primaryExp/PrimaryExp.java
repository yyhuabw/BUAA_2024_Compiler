package frontend.parser.ast.expression.primaryExp;

import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.expression.unaryExp.UnaryExpEle;
import middle.llvm_ir.IrValue;
import middle.symbol.value.ValueType;

public class PrimaryExp implements UnaryExpEle {
    private final SyntaxType type;
    private final PrimaryExpEle primaryExpEle;

    public PrimaryExp(PrimaryExpEle primaryExpEle) {
        this.type = SyntaxType.PRIMARY_EXP;
        this.primaryExpEle = primaryExpEle;
    }

    @Override
    public ValueType getValueType() {
        return primaryExpEle.getValueType();
    }

    @Override
    public int getDim() {
        return primaryExpEle.getDim();
    }

    @Override
    public String syntaxInfoOutput() {
        return primaryExpEle.syntaxInfoOutput() + type.getName() + "\n";
    }

    @Override
    public int evaluate() {
        return primaryExpEle.evaluate();
    }

    @Override
    public IrValue genIR() {
        return primaryExpEle.genIR();
    }
}
