package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.primaryExp.LVal;
import frontend.parser.ast.expression.single.Exp;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.memory.IrStoreInstr;
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
        IrValue expIR = exp.genVarIR(((IrPointerType) lValIR.getType()).getTargetType());
        new IrStoreInstr(expIR, lValIR);
        return null;
    }
}
