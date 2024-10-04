package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;

public class ContinueStmt implements StmtEle {
    private final Token continueTk;
    private final Token semicolon;

    public ContinueStmt(Token continueTk, Token semicolon) {
        this.continueTk = continueTk;
        this.semicolon = semicolon;
    }

    @Override
    public String syntaxInfoOutput() {
        return continueTk.syntaxInfoOutput() + semicolon.syntaxInfoOutput();
    }
}
