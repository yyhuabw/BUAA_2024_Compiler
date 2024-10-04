package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.primaryExp.LVal;

public class GetcharStmt implements StmtEle {
    private final LVal lVal;
    private final Token assign;
    private final Token getcharTk;
    private final Token leftParent;
    private final Token rightParent;
    private final Token semicolon;

    public GetcharStmt(LVal lVal, Token assign, Token getcharTk, Token leftParent, Token rightParent, Token semicolon) {
        this.lVal = lVal;
        this.assign = assign;
        this.getcharTk = getcharTk;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
        this.semicolon = semicolon;
    }

    @Override
    public String syntaxInfoOutput() {
        return lVal.syntaxInfoOutput()
                + assign.syntaxInfoOutput()
                + getcharTk.syntaxInfoOutput()
                + leftParent.syntaxInfoOutput()
                + rightParent.syntaxInfoOutput()
                + semicolon.syntaxInfoOutput();
    }
}
