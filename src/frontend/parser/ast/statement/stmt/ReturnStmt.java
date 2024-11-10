package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Exp;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.jump.IrRetInstr;
import middle.llvm_ir.instruction.type_change.IrTruncInstr;
import middle.llvm_ir.instruction.type_change.IrZextInstr;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.type.IrType;
import middle.llvm_ir.utils.constant.IrConstInt;

// 'return' [Exp] ';'
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
        IrValue expIR = exp.genIR();

        if (returnType.isINT8() && expIR.getType().isINT32()) {
            // expIR change to INT8
            if (expIR instanceof IrConstInt constInt) {
                expIR = new IrConstInt(IrIntType.INT8, constInt.getValue());
            } else {
                expIR = new IrTruncInstr(IrIntType.INT8, IrBuilder.getInstance().getLocalVarName(), expIR);
            }
        } else if (returnType.isINT32() && expIR.getType().isINT8()) {
            // expIR change to INT32
            if (expIR instanceof IrConstInt constInt) {
                expIR = new IrConstInt(IrIntType.INT32, constInt.getValue());
            } else {
                expIR = new IrZextInstr(IrIntType.INT32, IrBuilder.getInstance().getLocalVarName(), expIR);
            }
        }

        new IrRetInstr(expIR);
        return null;
    }
}
