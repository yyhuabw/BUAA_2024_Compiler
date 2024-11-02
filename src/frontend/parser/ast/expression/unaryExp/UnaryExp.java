package frontend.parser.ast.expression.unaryExp;

import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import middle.llvm_ir.IrValue;
import middle.symbol.value.ValueType;

public class UnaryExp implements SyntaxNode {
    private final SyntaxType type;
    private final UnaryExpEle unaryExpEle;

    public UnaryExp(UnaryExpEle unaryExpEle) {
        this.type = SyntaxType.UNARY_EXP;
        this.unaryExpEle = unaryExpEle;
    }

    public ValueType getValueType() {
        return unaryExpEle.getValueType();
    }

    public int getDim() {
        return unaryExpEle.getDim();
    }

    @Override
    public String syntaxInfoOutput() {
        return unaryExpEle.syntaxInfoOutput() + type.getName() + "\n";
    }

    @Override
    public IrValue genIR() {
        return unaryExpEle.genIR();
    }
}
