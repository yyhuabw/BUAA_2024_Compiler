package frontend.parser.ast.expression.unaryExp;

import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrAluInstr;
import middle.llvm_ir.instruction.IrIcmpInstr;
import middle.llvm_ir.instruction.type_change.IrZextInstr;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.utils.constant.IrConstInt;
import middle.symbol.value.ValueType;

public class UnaryOpExp implements UnaryExpEle {
    private final UnaryOp unaryOp;
    private final UnaryExp unaryExp;

    public UnaryOpExp(UnaryOp unaryOp, UnaryExp unaryExp) {
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    @Override
    public ValueType getValueType() {
        return unaryExp.getValueType();
    }

    @Override
    public int getDim() {
        return unaryExp.getDim();
    }

    @Override
    public String syntaxInfoOutput() {
        return unaryOp.syntaxInfoOutput() + unaryExp.syntaxInfoOutput();
    }

    @Override
    public int evaluate() {
        return switch (unaryOp.getOpType()) {
            case PLUS -> unaryExp.evaluate();
            case MINU -> -unaryExp.evaluate();
            case NOT -> unaryExp.evaluate() == 0 ? 1 : 0; // not use

            default -> 0; // Error
        };
    }

    @Override
    public IrValue genIR() {
        IrValue expValue = unaryExp.genIR();
        IrValue assistant = new IrConstInt(IrIntType.INT32, 0);

        return switch (unaryOp.getOpType()) {
            case PLUS -> expValue;
            case MINU -> new IrAluInstr(IrBuilder.getInstance().getLocalVarName(), IrAluInstr.Op.sub, assistant, expValue);
            case NOT -> dealCondExp(expValue, assistant);

            default -> null;
        };
    }

    private IrValue dealCondExp(IrValue expValue, IrValue assistant) {
        IrIcmpInstr icmpInstr = new IrIcmpInstr(IrBuilder.getInstance().getLocalVarName(), IrIcmpInstr.Op.eq, expValue, assistant);
        return new IrZextInstr(IrIntType.INT32, IrBuilder.getInstance().getLocalVarName(), icmpInstr);
    }
}
