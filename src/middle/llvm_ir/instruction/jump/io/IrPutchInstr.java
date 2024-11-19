package middle.llvm_ir.instruction.jump.io;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.MipsSyscallInstr;
import backend.mips.assembly.instruction.extended.MipsLiInstr;
import backend.mips.assembly.instruction.extended.MipsMoveInstr;
import backend.mips.assembly.instruction.memory.MipsLoadInstr;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrVoidType;
import middle.llvm_ir.utils.constant.IrConstInt;

public class IrPutchInstr extends IrIOInstr {
    public IrPutchInstr(IrValue value) {
        super(IrVoidType.VOID, "putch");
        addOperand(value);
    }

    public IrValue getValue() {
        return getOperand(0);
    }

    public static String getDeclare() {
        return "declare void @putch(i32)\n";
    }

    @Override
    public String irOutput() {
        return "call void @putch(i32 " +
                getValue().getName() + ")\n";
    }

    @Override
    public void genAsm() {
        super.genAsm();

        IrValue value = getValue();

        if (value instanceof IrConstInt constInt) {
            new MipsLiInstr(Register.A0, constInt.getValue());
        } else if (MipsBuilder.getInstance().getRegFor(value) != null) {
            new MipsMoveInstr(Register.A0, MipsBuilder.getInstance().getRegFor(value));
        } else { // load-from-stack
            new MipsLoadInstr(MipsLoadInstr.Op.lw, Register.A0, Register.SP, MipsBuilder.getInstance().getOffsetOf(value));
        }

        new MipsLiInstr(Register.V0, 11);
        new MipsSyscallInstr();
    }
}
