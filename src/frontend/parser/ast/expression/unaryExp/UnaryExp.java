package frontend.parser.ast.expression.unaryExp;

import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;

public class UnaryExp implements SyntaxNode {
    private final SyntaxType type;
    private final UnaryExpEle unaryExpEle;

    public UnaryExp(UnaryExpEle unaryExpEle) {
        this.type = SyntaxType.UNARY_EXP;
        this.unaryExpEle = unaryExpEle;
    }

    @Override
    public String syntaxInfoOutput() {
        return unaryExpEle.syntaxInfoOutput() + type.getName() + "\n";
    }
}
