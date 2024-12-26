package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Exp;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.jump.IrRetInstr;
import middle.llvm_ir.type.IrType;

// ReturnStmt → 'return' [Exp] ';'
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

    @Override
    public IrValue genIR() {
        if (exp == null) { // void
            new IrRetInstr(null);
            return null;
        }

        IrType returnType = IrBuilder.getInstance().getCurFunction().getReturnType();

        new IrRetInstr(exp.genVarIR(returnType));
        return null;
    }
}
