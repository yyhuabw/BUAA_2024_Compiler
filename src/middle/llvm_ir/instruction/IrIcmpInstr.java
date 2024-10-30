package middle.llvm_ir.instruction;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrIntType;

public class IrIcmpInstr extends IrInstruction {
    public enum Op {
        eq, // ==
        ne, // !=
        sgt, // >
        sge, // >=
        slt, // <
        sle // <=
    }

    private final Op op;

    public IrIcmpInstr(String name, Op op, IrValue operand1, IrValue operand2) {
        super(IrIntType.INT1, name, IrInstrType.ICMP);
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
        return getName() + " = icmp " + op.toString() + " " +
                getOperand1().getType().irOutput() + " " + // should be i32
                getOperand1().getName() + ", " +
                getOperand2().getName() + "\n";
    }
}
