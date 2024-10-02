package frontend.parser.ast.declaration.variable.initVal;

import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;

public class InitVal implements SyntaxNode {
    private final SyntaxType type;
    private final InitValEle initValEle;

    public InitVal(InitValEle initValEle) {
        this.type = SyntaxType.INIT_VAL;
        this.initValEle = initValEle;
    }

    @Override
    public String syntaxInfoOutput() {
        return initValEle.syntaxInfoOutput() + type.getName() + "\n";
    }
}
