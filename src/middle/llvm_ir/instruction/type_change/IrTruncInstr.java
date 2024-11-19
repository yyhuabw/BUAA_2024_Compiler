package middle.llvm_ir.instruction.type_change;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.alu.MipsRIAluInstr;
import backend.mips.assembly.instruction.memory.MipsLoadInstr;
import backend.mips.assembly.instruction.memory.MipsStoreInstr;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrType;

public class IrTruncInstr extends IrInstruction {
    public IrTruncInstr(IrType finalType, String name, IrValue originValue) {
        super(finalType, name, IrInstrType.TRUNC);
        addOperand(originValue);
    }

    public IrValue getOriginValue() {
        return getOperand(0);
    }

    @Override
    public boolean canBeUsed() {
        return true;
    }

    @Override
    public String irOutput() {
        return getName() + " = trunc " +
                getOriginValue().getType().irOutput() + " " +
                getOriginValue().getName() + " to " +
                getType().irOutput() + "\n";
    }

    @Override
    public void genAsm() {
        super.genAsm();

        /*
         * consider truncation
         * must i32 -> i8
         */
        Register oriReg = MipsBuilder.getInstance().getRegFor(getOriginValue());
        Register finalReg = MipsBuilder.getInstance().getRegFor(this);
        if (finalReg == null) {
            finalReg = Register.K0;
        }

        if (oriReg != null) {
            // truncation
            new MipsRIAluInstr(MipsRIAluInstr.Op.andi, finalReg, oriReg, 0xFF);
        } else { // oriValue in stack
            new MipsLoadInstr(MipsLoadInstr.Op.lb, finalReg, Register.SP, MipsBuilder.getInstance().getOffsetOf(getOriginValue()));
        }

        if (finalReg == Register.K0) {
            MipsBuilder.getInstance().downwardCurOffset(4);
            int curOffset = MipsBuilder.getInstance().getCurStackOffset();
            MipsBuilder.getInstance().addValueMapping(this, curOffset);
            new MipsStoreInstr(MipsStoreInstr.Op.sw, finalReg, Register.SP, curOffset);
        }
    }
}
