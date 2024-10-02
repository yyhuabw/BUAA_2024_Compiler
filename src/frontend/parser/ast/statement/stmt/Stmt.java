package frontend.parser.ast.statement.stmt;

import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.statement.block.BlockItemEle;

public class Stmt implements BlockItemEle {
    private final SyntaxType type;
    private final StmtEle stmtEle;

    public Stmt(StmtEle stmtEle) {
        this.type = SyntaxType.STMT;
        this.stmtEle = stmtEle;
    }

    @Override
    public String syntaxInfoOutput() {
        return stmtEle.syntaxInfoOutput() + type.getName() + "\n";
    }
}
