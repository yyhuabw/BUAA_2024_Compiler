package frontend.parser.ast.expression.primaryExp;

import frontend.parser.ast.SyntaxNode;
import middle.llvm_ir.IrValue;
import middle.symbol.value.ValueType;

public interface PrimaryExpEle extends SyntaxNode {
    ValueType getValueType();

    int getDim();

    @Override
    IrValue genIR();
}
