package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Exp;

public class ReturnStmt implements StmtEle {
    private final Token returnTk;
    private Exp exp = null;
    private final Token semicolon;

    public ReturnStmt(Token returnTk, Token semicolon) {
        this.returnTk = returnTk;
        this.semicolon = semicolon;
    }

    public ReturnStmt(Token returnTk, Exp exp, Token semicolon) {
        this(returnTk, semicolon);
        this.exp = exp;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(returnTk.syntaxInfoOutput());
        if (exp != null) {
            sb.append(exp.syntaxInfoOutput());
        }
        sb.append(semicolon.syntaxInfoOutput());
        return sb.toString();
    }
}
