package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.primaryExp.LVal;

public class GetintStmt implements StmtEle {
    private final LVal lVal;
    private final Token assign;
    private final Token getintTk;
    private final Token leftParent;
    private final Token rightParent;
    private final Token semicolon;

    public GetintStmt(LVal lVal, Token assign, Token getintTk, Token leftParent, Token rightParent, Token semicolon) {
        this.lVal = lVal;
        this.assign = assign;
        this.getintTk = getintTk;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
        this.semicolon = semicolon;
    }

    @Override
    public String syntaxInfoOutput() {
        return lVal.syntaxInfoOutput()
                + assign.syntaxInfoOutput()
                + getintTk.syntaxInfoOutput()
                + leftParent.syntaxInfoOutput()
                + rightParent.syntaxInfoOutput()
                + semicolon.syntaxInfoOutput();
    }
}
