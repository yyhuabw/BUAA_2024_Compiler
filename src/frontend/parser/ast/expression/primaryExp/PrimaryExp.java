package frontend.parser.ast.expression.primaryExp;

import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.expression.unaryExp.UnaryExpEle;

public class PrimaryExp implements UnaryExpEle {
    private final SyntaxType type;
    private final PrimaryExpEle primaryExpEle;

    public PrimaryExp(PrimaryExpEle primaryExpEle) {
        this.type = SyntaxType.PRIMARY_EXP;
        this.primaryExpEle = primaryExpEle;
    }

    @Override
    public String syntaxInfoOutput() {
        return primaryExpEle.syntaxInfoOutput() + type.getName() + "\n";
    }
}
