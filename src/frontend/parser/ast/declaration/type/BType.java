package frontend.parser.ast.declaration.type;

import frontend.parser.ast.SyntaxNode;

public class BType implements SyntaxNode {
    private final BTypeEle bTypeEle;

    public BType(BTypeEle bTypeEle) {
        this.bTypeEle = bTypeEle;
    }

    @Override
    public String syntaxInfoOutput() {
        return bTypeEle.syntaxInfoOutput();
    }
}
