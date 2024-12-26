package middle.llvm_ir.instruction;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.alu.MipsMFHiLoInstr;
import backend.mips.assembly.instruction.alu.MipsMulDivInstr;
import backend.mips.assembly.instruction.alu.MipsRRAluInstr;
import backend.mips.assembly.instruction.extended.MipsLiInstr;
import backend.mips.assembly.instruction.memory.MipsLoadInstr;
import backend.mips.assembly.instruction.memory.MipsStoreInstr;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.utils.constant.IrConstInt;

public class IrAluInstr extends IrInstruction {
    public enum Op {
        add,
        sub,
        mul,
        sdiv,
        srem,
        and,
        or
    }

    private final Op op;

    public IrAluInstr(String name, Op op, IrValue operand1, IrValue operand2) {
        super(IrIntType.INT32, name, IrInstrType.ALU);
        this.op = op;
        addOperand(operand1);
        addOperand(operand2);
    }

    public Op getOp() {
        return op;
    }

    public IrValue getOperand1() {
        return getOperand(0);
    }

    public IrValue getOperand2() {
        return getOperand(1);
    }

    public String getGVNHash() {
        String operand1Name = getOperand1().getName();
        String operand2Name = getOperand2().getName();

        if (op == Op.add || op == Op.mul) { // operands are interchangeable
            if (operand1Name.compareTo(operand2Name) < 0) {
                return operand1Name + " " + op + " " + operand2Name;
            } else {
                return operand2Name + " " + op + " " + operand1Name;
            }
        } else {
            return operand1Name + " " + op + " " + operand2Name;
        }
    }

    @Override
    public boolean canBeUsed() {
        return true;
    }

    @Override
    public String irOutput() {
        return getName() + " = " + op.toString() + " " +
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
            case add:
                new MipsRRAluInstr(MipsRRAluInstr.Op.addu, result, reg1, reg2);
                break;
            case sub:
                new MipsRRAluInstr(MipsRRAluInstr.Op.subu, result, reg1, reg2);
                break;
            case mul:
                new MipsMulDivInstr(MipsMulDivInstr.Op.mult, reg1, reg2);
                // no-need mfhi
                new MipsMFHiLoInstr(MipsMFHiLoInstr.Op.mflo, result);
                break;
            case sdiv:
                new MipsMulDivInstr(MipsMulDivInstr.Op.div, reg1, reg2);
                new MipsMFHiLoInstr(MipsMFHiLoInstr.Op.mflo, result);
                break;
            case srem:
                new MipsMulDivInstr(MipsMulDivInstr.Op.div, reg1, reg2);
                new MipsMFHiLoInstr(MipsMFHiLoInstr.Op.mfhi, result);
                break;
            case and:
                new MipsRRAluInstr(MipsRRAluInstr.Op.and, result, reg1, reg2);
                break;
            case or:
                new MipsRRAluInstr(MipsRRAluInstr.Op.or, result, reg1, reg2);
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
