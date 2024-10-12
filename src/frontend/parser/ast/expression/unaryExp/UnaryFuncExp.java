package frontend.parser.ast.expression.unaryExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.FuncRParams;
import frontend.parser.ast.terminal.Ident;
import middle.symbol.value.ValueType;

public class UnaryFuncExp implements UnaryExpEle {
    private final Ident ident;
    private final Token leftParent;
    private FuncRParams funcRParams = null;
    private final Token rightParent;

    public UnaryFuncExp(Ident ident, Token leftParent, Token rightParent) {
        this.ident = ident;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
    }

    public UnaryFuncExp(Ident ident, Token leftParent, FuncRParams funcRParams, Token rightParent) {
        this(ident, leftParent, rightParent);
        this.funcRParams = funcRParams;
    }

    @Override
    public ValueType getValueType() {
        return ident.queryValueType();
    }

    @Override
    public int getDim() {
        return ident.queryDim();
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident.syntaxInfoOutput());
        sb.append(leftParent.syntaxInfoOutput());
        if (funcRParams != null) {
            sb.append(funcRParams.syntaxInfoOutput());
        }
        sb.append(rightParent.syntaxInfoOutput());
        return sb.toString();
    }
}
