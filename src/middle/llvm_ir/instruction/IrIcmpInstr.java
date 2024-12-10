package middle.llvm_ir.instruction;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.MipsCmpInstr;
import backend.mips.assembly.instruction.extended.MipsLiInstr;
import backend.mips.assembly.instruction.memory.MipsLoadInstr;
import backend.mips.assembly.instruction.memory.MipsStoreInstr;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.utils.constant.IrConstInt;

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

    public String getGVNHash() {
        return "icmp " + getOperand1().getName() + " " + op + " " + getOperand2().getName();
    }

    @Override
    public boolean canBeUsed() {
        return true;
    }

    @Override
    public String irOutput() {
        return getName() + " = icmp " + op.toString() + " " +
                getOperand1().getType().irOutput() + " " + // should be i32
                getOperand1().getName() + ", " +
                getOperand2().getName() + "\n";
    }

    @Override
    public void genAsm() {
        super.genAsm();

        Register reg1 = operand2reg(getOperand1(), Register.K0);
        Register reg2 = operand2reg(getOperand2(), Register.K1);

        Register result = MipsBuilder.getInstance().getRegFor(this);
        if (result == null) {
            result = Register.K0;
        }

        switch (op) {
            case eq:
                new MipsCmpInstr(MipsCmpInstr.Op.seq, result, reg1, reg2);
                break;
            case ne:
                new MipsCmpInstr(MipsCmpInstr.Op.sne, result, reg1, reg2);
                break;
            case sgt:
                new MipsCmpInstr(MipsCmpInstr.Op.sgt, result, reg1, reg2);
                break;
            case sge:
                new MipsCmpInstr(MipsCmpInstr.Op.sge, result, reg1, reg2);
                break;
            case slt:
                new MipsCmpInstr(MipsCmpInstr.Op.slt, result, reg1, reg2);
                break;
            case sle:
                new MipsCmpInstr(MipsCmpInstr.Op.sle, result, reg1, reg2);
                break;
        }

        // store-to-stack
        if (result == Register.K0) {
            MipsBuilder.getInstance().downwardCurOffset(4);
            int curOffset = MipsBuilder.getInstance().getCurStackOffset();
            MipsBuilder.getInstance().addValueMapping(this, curOffset);
            new MipsStoreInstr(MipsStoreInstr.Op.sw, result, Register.SP, curOffset);
        }
    }

    private Register operand2reg(IrValue operand, Register tmpReg) {
        if (operand instanceof IrConstInt constInt) {
            new MipsLiInstr(tmpReg, constInt.getValue());
            return tmpReg;
        }

        Register reg = MipsBuilder.getInstance().getRegFor(operand);
        if (reg != null) { // have reg
            return reg;
        }

        // need load-from-stack
        new MipsLoadInstr(MipsLoadInstr.Op.lw, tmpReg, Register.SP, MipsBuilder.getInstance().getOrSetOffsetOf(operand));
        return tmpReg;
    }
}
