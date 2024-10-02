package frontend.parser.ast.statement.stmt;

import frontend.lexer.Token;
import frontend.parser.ast.expression.single.Exp;

public class ExpStmt implements StmtEle {
    private Exp exp = null;
    private final Token semicolon;

    public ExpStmt(Token semicolon) {
        this.semicolon = semicolon;
    }

    public ExpStmt(Exp exp, Token semicolon) {
        this(semicolon);
        this.exp = exp;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        if (exp != null) {
            sb.append(exp.syntaxInfoOutput());
        }
        sb.append(semicolon.syntaxInfoOutput());
        return sb.toString();
    }
}
