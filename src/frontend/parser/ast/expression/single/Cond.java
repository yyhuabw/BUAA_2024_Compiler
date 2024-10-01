package frontend.parser.ast.expression.single;

import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.expression.opExp.LOrExp;

public class Cond implements SyntaxNode {
    private final SyntaxType type;
    private final LOrExp lOrExp;

    public Cond(LOrExp lOrExp) {
        this.type = SyntaxType.COND_EXP;
        this.lOrExp = lOrExp;
    }

    @Override
    public String syntaxInfoOutput() {
        return lOrExp.syntaxInfoOutput() + type.getName() + "\n";
    }
}
