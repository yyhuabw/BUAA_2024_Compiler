package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Exp;

public class ExpStmt implements StmtEle {
    private final Exp exp;
    private final Token semicolon;

    public ExpStmt(Exp exp, Token semicolon) {
        this.exp = exp;
        this.semicolon = semicolon;
    }

    @Override
    public String syntaxInfoOutput() {
        return exp.syntaxInfoOutput() + semicolon.syntaxInfoOutput();
    }
}
