package frontend.parser.ast.statement.stmt;

import frontend.lexer.Token;

public class NullStmt implements StmtEle {
    private final Token semicolon;

    public NullStmt(Token semicolon) {
        this.semicolon = semicolon;
    }

    @Override
    public String syntaxInfoOutput() {
        return semicolon.syntaxInfoOutput();
    }
}
