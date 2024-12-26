package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.primaryExp.LVal;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.jump.io.IrGetintInstr;
import middle.llvm_ir.instruction.memory.IrStoreInstr;
import middle.llvm_ir.instruction.type_change.IrTruncInstr;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.type.IrPointerType;

// GetintStmt → LVal '=' 'getint''('')'';'
public class GetintStmt implements StmtEle {
    private final LVal lVal;
    private final Token assign;
    private final Token getintTk;
    private final Token leftParent;
    private final Token rightParent;
    private final Token semicolon;

    public GetintStmt(LVal lVal, Token assign, Token getintTk, Token leftParent, Token rightParent, Token semicolon) {
        this.lVal = lVal;
        this.assign = assign;
        this.getintTk = getintTk;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
        this.semicolon = semicolon;
    }

    @Override
    public String syntaxInfoOutput() {
        return lVal.syntaxInfoOutput()
                + assign.syntaxInfoOutput()
                + getintTk.syntaxInfoOutput()
                + leftParent.syntaxInfoOutput()
                + rightParent.syntaxInfoOutput()
                + semicolon.syntaxInfoOutput();
    }

    /**
     * void
     * @return null
     */
    @Override
    public IrValue genIR() {
        IrValue lValIR = lVal.genIRForAssign();
        IrValue getintInstr = new IrGetintInstr(IrBuilder.getInstance().getLocalVarName());
        if (((IrPointerType) lValIR.getType()).getTargetType().isINT8()) {
            getintInstr = new IrTruncInstr(IrIntType.INT8, IrBuilder.getInstance().getLocalVarName(), getintInstr);
        }
        new IrStoreInstr(getintInstr, lValIR);
        return null;
    }
}
