package frontend.parser.ast.expression.unaryExp;

public class UnaryOpExp implements UnaryExpEle {
    private final UnaryOp unaryOp;
    private final UnaryExp unaryExp;

    public UnaryOpExp(UnaryOp unaryOp, UnaryExp unaryExp) {
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    @Override
    public String syntaxInfoOutput() {
        return unaryOp.syntaxInfoOutput() + unaryExp.syntaxInfoOutput();
    }
}
