package frontend.parser.ast.statement.stmt;

import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.statement.block.BlockItemEle;
import middle.llvm_ir.IrValue;

public class Stmt implements BlockItemEle {
    private final SyntaxType type;
    private final StmtEle stmtEle;

    public Stmt(StmtEle stmtEle) {
        this.type = SyntaxType.STMT;
        this.stmtEle = stmtEle;
    }

    public StmtEle getStmtEle() {
        return stmtEle;
    }

    @Override
    public String syntaxInfoOutput() {
        return stmtEle.syntaxInfoOutput() + type.getName() + "\n";
    }

    @Override
    public IrValue genIR() {
        return stmtEle.genIR();
    }
}
