package frontend.parser.ast.expression.primaryExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Exp;

public class ParentExp implements PrimaryExpEle {
    private final Token leftParent;
    private final Exp exp;
    private final Token rightParent;

    public ParentExp(Token leftParent, Exp exp, Token rightParent) {
        this.leftParent = leftParent;
        this.exp = exp;
        this.rightParent = rightParent;
    }

    @Override
    public String syntaxInfoOutput() {
        return leftParent.syntaxInfoOutput() + exp.syntaxInfoOutput() + rightParent.syntaxInfoOutput();
    }
}
