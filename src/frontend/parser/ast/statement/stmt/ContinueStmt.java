package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.jump.br.IrDirtBrInstr;

// ContinueStmt →  'continue' ';'
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

    // void
    @Override
    public IrValue genIR() {
        new IrDirtBrInstr(IrBuilder.getInstance().getCurLoop().getStepBlock());
        return null;
    }
}
