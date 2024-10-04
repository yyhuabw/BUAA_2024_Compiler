package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Cond;

public class IfStmt implements StmtEle {
    private final Token ifTk;
    private final Token leftParent;
    private final Cond cond;
    private final Token rightParent;
    private final Stmt ifStmt;
    private Token elseTk = null;
    private Stmt elseStmt = null;

    public IfStmt(Token ifTk, Token leftParent, Cond cond, Token rightParent, Stmt ifStmt) {
        this.ifTk = ifTk;
        this.leftParent = leftParent;
        this.cond = cond;
        this.rightParent = rightParent;
        this.ifStmt = ifStmt;
    }

    public IfStmt(Token ifTk, Token leftParent, Cond cond, Token rightParent, Stmt ifStmt, Token elseTk, Stmt elseStmt) {
        this(ifTk, leftParent, cond, rightParent, ifStmt);
        this.elseTk = elseTk;
        this.elseStmt = elseStmt;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(ifTk.syntaxInfoOutput());
        sb.append(leftParent.syntaxInfoOutput());
        sb.append(cond.syntaxInfoOutput());
        sb.append(rightParent.syntaxInfoOutput());
        sb.append(ifStmt.syntaxInfoOutput());
        if (elseTk != null) {
            sb.append(elseTk.syntaxInfoOutput());
            sb.append(elseStmt.syntaxInfoOutput());
        }
        return sb.toString();
    }
}
