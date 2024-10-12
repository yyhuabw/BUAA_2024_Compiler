package frontend.parser.ast.expression.unaryExp;

import frontend.parser.ast.SyntaxNode;
import middle.symbol.value.ValueType;

public interface UnaryExpEle extends SyntaxNode {
    ValueType getValueType();

    int getDim();
}
