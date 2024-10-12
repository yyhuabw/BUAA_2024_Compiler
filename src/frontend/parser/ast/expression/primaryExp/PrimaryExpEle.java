package frontend.parser.ast.expression.primaryExp;

import frontend.parser.ast.SyntaxNode;
import middle.symbol.value.ValueType;

public interface PrimaryExpEle extends SyntaxNode {
    ValueType getValueType();

    int getDim();
}
