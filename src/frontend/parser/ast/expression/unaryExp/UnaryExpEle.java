package frontend.parser.ast.expression.unaryExp;

import frontend.parser.ast.SyntaxNode;
import middle.llvm_ir.IrValue;
import middle.symbol.value.ValueType;

public interface UnaryExpEle extends SyntaxNode {
    ValueType getValueType();

    int getDim();

    @Override
    IrValue genIR();
}
