package middle.llvm_ir.instruction.type_change;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.memory.MipsStoreInstr;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrType;

public class IrZextInstr extends IrInstruction {
    public IrZextInstr(IrType finalType, String name, IrValue originValue) {
        super(finalType, name, IrInstrType.ZEXT);
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
        return getName() + " = zext " +
                getOriginValue().getType().irOutput() + " " +
                getOriginValue().getName() + " to " +
                getType().irOutput() + "\n";
    }

    @Override
    public void genAsm() {
        super.genAsm();

        // direct mapping
        Register oriReg = MipsBuilder.getInstance().getRegFor(getOriginValue());
        if (oriReg != null) {
            MipsBuilder.getInstance().downwardCurOffset(4);
            int curOffset = MipsBuilder.getInstance().getCurStackOffset();
            MipsBuilder.getInstance().addValueMapping(this, curOffset);
            new MipsStoreInstr(MipsStoreInstr.Op.sw, oriReg, Register.SP, curOffset);
        } else { // oriValue in stack
            MipsBuilder.getInstance().addValueMapping(this, MipsBuilder.getInstance().getOrSetOffsetOf(getOriginValue()));
        }
    }
}
