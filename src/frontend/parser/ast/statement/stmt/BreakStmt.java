package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.jump.br.IrDirtBrInstr;

// BreakStmt → 'break' ';'
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

    // void
    @Override
    public IrValue genIR() {
        new IrDirtBrInstr(IrBuilder.getInstance().getCurLoop().getFollowBlock());
        return null;
    }
}
