package frontend.parser.ast.function.funcType;

import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;

public class FuncType implements SyntaxNode {
    private final SyntaxType type;
    private final FuncTypeEle funcTypeEle;

    public FuncType(FuncTypeEle funcTypeEle) {
        this.type = SyntaxType.FUNC_TYPE;
        this.funcTypeEle = funcTypeEle;
    }

    @Override
    public String syntaxInfoOutput() {
        return funcTypeEle.syntaxInfoOutput() + type.getName() + "\n";
    }
}
