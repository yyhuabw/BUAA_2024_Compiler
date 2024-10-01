package frontend.parser.ast.declaration.constant.constInitVal;

import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;

public class ConstInitVal implements SyntaxNode {
    private final SyntaxType type;
    private final ConstInitValEle constInitValEle;

    public ConstInitVal(ConstInitValEle constInitValEle) {
        this.type = SyntaxType.CONST_INITVAL;
        this.constInitValEle = constInitValEle;
    }

    @Override
    public String syntaxInfoOutput() {
        return constInitValEle.syntaxInfoOutput() + type.getName() + "\n";
    }
}
