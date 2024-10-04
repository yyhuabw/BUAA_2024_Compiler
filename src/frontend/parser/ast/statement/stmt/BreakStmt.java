package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;

public class BreakStmt implements StmtEle {
    private final Token breakTk;
    private final Token semicolon;

    public BreakStmt(Token breakTk, Token semicolon) {
        this.breakTk = breakTk;
        this.semicolon = semicolon;
    }

    @Override
    public String syntaxInfoOutput() {
        return breakTk.syntaxInfoOutput() + semicolon.syntaxInfoOutput();
    }
}
