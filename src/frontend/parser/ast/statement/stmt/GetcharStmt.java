package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.primaryExp.LVal;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.jump.io.IrGetcharInstr;
import middle.llvm_ir.instruction.memory.IrStoreInstr;
import middle.llvm_ir.instruction.type_change.IrTruncInstr;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.type.IrPointerType;

// LVal '=' 'getchar''('')'';'
public class GetcharStmt implements StmtEle {
    private final LVal lVal;
    private final Token assign;
    private final Token getcharTk;
    private final Token leftParent;
    private final Token rightParent;
    private final Token semicolon;

    public GetcharStmt(LVal lVal, Token assign, Token getcharTk, Token leftParent, Token rightParent, Token semicolon) {
        this.lVal = lVal;
        this.assign = assign;
        this.getcharTk = getcharTk;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
        this.semicolon = semicolon;
    }

    @Override
    public String syntaxInfoOutput() {
        return lVal.syntaxInfoOutput()
                + assign.syntaxInfoOutput()
                + getcharTk.syntaxInfoOutput()
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
        IrValue getcharInstr = new IrGetcharInstr(IrBuilder.getInstance().getLocalVarName()); // i32
        if (((IrPointerType) lValIR.getType()).getTargetType().isINT8()) {
            getcharInstr = new IrTruncInstr(IrIntType.INT8, IrBuilder.getInstance().getLocalVarName(), getcharInstr);
        }
        new IrStoreInstr(getcharInstr, lValIR);
        return null;
    }
}
