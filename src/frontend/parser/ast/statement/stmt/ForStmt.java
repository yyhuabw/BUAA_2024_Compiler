package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.expression.primaryExp.LVal;
import frontend.parser.ast.expression.single.Exp;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.memory.IrStoreInstr;
import middle.llvm_ir.type.IrPointerType;

public class ForStmt implements SyntaxNode {
    private final SyntaxType type;
    private final LVal lVal;
    private final Token assign;
    private final Exp exp;

    public ForStmt(LVal lVal, Token assign, Exp exp) {
        this.type = SyntaxType.FOR_STMT;
        this.lVal = lVal;
        this.assign = assign;
        this.exp = exp;
    }

    @Override
    public String syntaxInfoOutput() {
        return lVal.syntaxInfoOutput() + assign.syntaxInfoOutput() + exp.syntaxInfoOutput() + type.getName() + "\n";
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
