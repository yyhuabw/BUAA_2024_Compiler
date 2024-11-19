package middle.llvm_ir.instruction.jump.io;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.MipsSyscallInstr;
import backend.mips.assembly.instruction.extended.MipsLiInstr;
import backend.mips.assembly.instruction.extended.MipsMoveInstr;
import backend.mips.assembly.instruction.memory.MipsStoreInstr;
import middle.llvm_ir.type.IrIntType;

public class IrGetcharInstr extends IrIOInstr {
    public IrGetcharInstr(String name) {
        super(IrIntType.INT32, name);
    }

    public static String getDeclare() {
        return "declare i32 @getchar()\n";
    }

    @Override
    public boolean canBeUsed() {
        return true;
    }

    @Override
    public String irOutput() {
        return getName() + " = call i32 @getchar()\n";
    }

    @Override
    public void genAsm() {
        super.genAsm();

        new MipsLiInstr(Register.V0, 12);
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
