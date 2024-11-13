package middle.llvm_ir.instruction;

import backend.mips.assembly.instruction.MipsComment;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrIntType;

public class IrAluInstr extends IrInstruction {
    public enum Op {
        add,
        sub,
        mul,
        sdiv,
        srem
        // and
        // or
    }

    private final Op op;

    public IrAluInstr(String name, Op op, IrValue operand1, IrValue operand2) {
        super(IrIntType.INT32, name, IrInstrType.ALU);
        this.op = op;
        addOperand(operand1);
        addOperand(operand2);
    }

    public IrValue getOperand1() {
        return getOperand(0);
    }

    public IrValue getOperand2() {
        return getOperand(1);
    }

    @Override
    public String irOutput() {
        return getName() + " = " + op.toString() + " " +
                getOperand1().getType().irOutput() + " " + // should be i32
                getOperand1().getName() + ", " +
                getOperand2().getName() + "\n";
    }

    @Override
    public void genMIPS() {
        new MipsComment(IrAluInstr.class.getSimpleName() + ": " + op.toString());
        /*TODO*/
    }
}
