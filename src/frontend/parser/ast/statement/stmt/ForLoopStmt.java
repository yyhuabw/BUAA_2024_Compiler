package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Cond;

public class ForLoopStmt implements StmtEle {
    private final Token forTk;
    private final Token leftParent;
    private ForStmt forStmt1 = null;
    private final Token semicolon1;
    private Cond cond = null;
    private final Token semicolon2;
    private ForStmt forStmt2 = null;
    private final Token rightParent;
    private final Stmt stmt;

    public ForLoopStmt(Token forTk,
                       Token leftParent,
                       Token semicolon1,
                       Token semicolon2,
                       Token rightParent,
                       Stmt stmt) {
        this.forTk = forTk;
        this.leftParent = leftParent;
        this.semicolon1 = semicolon1;
        this.semicolon2 = semicolon2;
        this.rightParent = rightParent;
        this.stmt = stmt;
    }

    public ForLoopStmt(Token forTk,
                       Token leftParent,
                       ForStmt forStmt1,
                       Token semicolon1,
                       Cond cond,
                       Token semicolon2,
                       ForStmt forStmt2,
                       Token rightParent,
                       Stmt stmt) {
        this(forTk, leftParent, semicolon1, semicolon2, rightParent, stmt);
        this.forStmt1 = forStmt1;
        this.cond = cond;
        this.forStmt2 = forStmt2;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(forTk.syntaxInfoOutput());
        sb.append(leftParent.syntaxInfoOutput());
        if (forStmt1 != null) {
            sb.append(forStmt1.syntaxInfoOutput());
        }
        sb.append(semicolon1.syntaxInfoOutput());
        if (cond != null) {
            sb.append(cond.syntaxInfoOutput());
        }
        sb.append(semicolon2.syntaxInfoOutput());
        if (forStmt2 != null) {
            sb.append(forStmt2.syntaxInfoOutput());
        }
        sb.append(rightParent.syntaxInfoOutput());
        sb.append(stmt.syntaxInfoOutput());
        return sb.toString();
    }
}
