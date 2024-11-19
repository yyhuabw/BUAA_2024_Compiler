package middle.llvm_ir.instruction.jump.io;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.MipsSyscallInstr;
import backend.mips.assembly.instruction.extended.MipsLiInstr;
import backend.mips.assembly.instruction.extended.MipsMoveInstr;
import backend.mips.assembly.instruction.memory.MipsStoreInstr;
import middle.llvm_ir.type.IrIntType;

public class IrGetintInstr extends IrIOInstr {
    public IrGetintInstr(String name) {
        super(IrIntType.INT32, name);
    }

    public static String getDeclare() {
        return "declare i32 @getint()\n";
    }

    @Override
    public boolean canBeUsed() {
        return true;
    }

    @Override
    public String irOutput() {
        return getName() + " = call i32 @getint()\n";
    }

    @Override
    public void genAsm() {
        super.genAsm();

        new MipsLiInstr(Register.V0, 5);
        new MipsSyscallInstr();

        Register reg = MipsBuilder.getInstance().getRegFor(this);
        if (reg != null) {
            new MipsMoveInstr(reg, Register.V0);
        } else { // store-to-stack
            MipsBuilder.getInstance().downwardCurOffset(4);
            int curOffset = MipsBuilder.getInstance().getCurStackOffset();
            MipsBuilder.getInstance().addValueMapping(this, curOffset);
            new MipsStoreInstr(MipsStoreInstr.Op.sw, Register.V0, Register.SP, curOffset);
        }
    }
}
