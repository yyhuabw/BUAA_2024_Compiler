package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.primaryExp.LVal;
import frontend.parser.ast.expression.single.Exp;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.memory.IrStoreInstr;
import middle.llvm_ir.instruction.type_change.IrTruncInstr;
import middle.llvm_ir.instruction.type_change.IrZextInstr;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.type.IrPointerType;

public class AssignStmt implements StmtEle {
    private final LVal lVal;
    private final Token assign;
    private final Exp exp;
    private final Token semicolon;

    public AssignStmt(LVal lVal, Token assign, Exp exp, Token semicolon) {
        this.lVal = lVal;
        this.assign = assign;
        this.exp = exp;
        this.semicolon = semicolon;
    }

    @Override
    public String syntaxInfoOutput() {
        return lVal.syntaxInfoOutput() + assign.syntaxInfoOutput() + exp.syntaxInfoOutput() + semicolon.syntaxInfoOutput();
    }

    // void
    @Override
    public IrValue genIR() {
        IrValue lValIR = lVal.genIRForAssign();
        IrValue expIR = exp.genIR(); // should be i32
        if (expIR.getType().isINT32() && ((IrPointerType) lValIR.getType()).getTargetType().isINT8()) { // LVal i8 = exp i32
            expIR = new IrTruncInstr(IrIntType.INT8, IrBuilder.getInstance().getLocalVarName(), expIR);
        } else if (expIR.getType().isINT8() && ((IrPointerType) lValIR.getType()).getTargetType().isINT32()) { // LVal i32 = exp i8
            expIR = new IrZextInstr(IrIntType.INT32, IrBuilder.getInstance().getLocalVarName(), expIR);
        }
        new IrStoreInstr(expIR, lValIR);
        return null;
    }
}
