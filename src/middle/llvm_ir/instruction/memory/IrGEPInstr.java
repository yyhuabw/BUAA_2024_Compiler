package middle.llvm_ir.instruction.memory;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.alu.MipsRIAluInstr;
import backend.mips.assembly.instruction.alu.MipsRRAluInstr;
import backend.mips.assembly.instruction.extended.MipsLaInstr;
import backend.mips.assembly.instruction.memory.MipsLoadInstr;
import backend.mips.assembly.instruction.memory.MipsStoreInstr;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrPointerType;
import middle.llvm_ir.type.IrType;
import middle.llvm_ir.utils.IrGlobalVar;
import middle.llvm_ir.utils.constant.IrConstInt;

/**
 * <result> = getelementptr inbounds <ty>, <ty>* <ptrval>{, [inrange] <ty> <idx>}*
 */
public class IrGEPInstr extends IrInstruction {
    public IrGEPInstr(IrPointerType type, String name, IrValue ptrValue, IrValue index) {
        super(type, name, IrInstrType.GEP);
        addOperand(ptrValue);
        addOperand(index);
    }

    public IrValue getPtrValue() {
        return getOperand(0);
    }

    public IrValue getIndex() {
        return getOperand(1);
    }

    public String getGVNHash() {
        return "gep " + getPtrValue().getName() + " " + getIndex().getName();
    }

    @Override
    public boolean canBeUsed() {
        return true;
    }

    @Override
    public String irOutput() {
        IrValue ptrValue = getPtrValue();
        IrValue index = getIndex();
        IrPointerType ptrType = (IrPointerType) ptrValue.getType();
        IrType trgtType = ptrType.getTargetType();

        if (trgtType.isArray()) {
            return getName() + " = getelementptr inbounds " +
                    trgtType.irOutput() + ", " +
                    ptrType.irOutput() + " " +
                    ptrValue.getName() + ", i32 0, " +
                    index.getType().irOutput() + " " +
                    index.getName() + "\n"; // the type of index should be i32
        } else {
            return getName() + " = getelementptr inbounds " +
                    trgtType.irOutput() + ", " +
                    ptrType.irOutput() + " " +
                    ptrValue.getName() + ", " +
                    index.getType().irOutput() + " " +
                    index.getName() + "\n"; // the type of index should be i32
        }
    }

    @Override
    public void genAsm() {
        super.genAsm();

        Register ptrReg = ptrVal2reg(getPtrValue());
        Register indexReg = Register.K1;

        Register result = MipsBuilder.getInstance().getRegFor(this);
        if (result == null) {
            result = Register.K0;
        }

        IrValue index = getIndex();

        if (index instanceof IrConstInt constInt) {
            // base + index * 4
            new MipsRIAluInstr(MipsRIAluInstr.Op.addiu, result, ptrReg, constInt.getValue() * 4);
        } else {
            if (MipsBuilder.getInstance().getRegFor(index) != null) {
                indexReg = MipsBuilder.getInstance().getRegFor(index);
            } else {
                new MipsLoadInstr(MipsLoadInstr.Op.lw, indexReg, Register.SP, MipsBuilder.getInstance().getOffsetOf(index));
            }
            // base + index << 2
            new MipsRIAluInstr(MipsRIAluInstr.Op.sll, Register.K1, indexReg, 2);
            new MipsRRAluInstr(MipsRRAluInstr.Op.addu, result, ptrReg, Register.K1);
        }

        // store-to-stack
        if (result == Register.K0) {
            MipsBuilder.getInstance().downwardCurOffset(4);
            int curOffset = MipsBuilder.getInstance().getCurStackOffset();
            MipsBuilder.getInstance().addValueMapping(this, curOffset);
            new MipsStoreInstr(MipsStoreInstr.Op.sw, result, Register.SP, curOffset);
        }
    }

    // ptrValue -> address
    // tmpReg -> k0
    private Register ptrVal2reg(IrValue ptrValue) {
        if (ptrValue instanceof IrGlobalVar) {
            new MipsLaInstr(Register.K0, ptrValue.getName().substring(1));
            return Register.K0;
        }

        Register reg = MipsBuilder.getInstance().getRegFor(ptrValue);
        if (reg != null) {
            return reg;
        }

        // load-from-stack
        new MipsLoadInstr(MipsLoadInstr.Op.lw, Register.K0, Register.SP, MipsBuilder.getInstance().getOffsetOf(ptrValue));
        return Register.K0;
    }
}
