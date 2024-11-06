package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import middle.llvm_ir.IrValue;

public class NullStmt implements StmtEle {
    private final Token semicolon;

    public NullStmt(Token semicolon) {
        this.semicolon = semicolon;
    }

    @Override
    public String syntaxInfoOutput() {
        return semicolon.syntaxInfoOutput();
    }

    @Override
    public IrValue genIR() {
        return null;
    }
}
