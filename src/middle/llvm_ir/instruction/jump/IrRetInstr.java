package middle.llvm_ir.instruction.jump;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.MipsComment;
import backend.mips.assembly.instruction.MipsSyscallInstr;
import backend.mips.assembly.instruction.extended.MipsLiInstr;
import backend.mips.assembly.instruction.extended.MipsMoveInstr;
import backend.mips.assembly.instruction.jump.MipsJumpInstr;
import backend.mips.assembly.instruction.memory.MipsLoadInstr;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrVoidType;
import middle.llvm_ir.utils.constant.IrConstInt;

/**
 *  ret <type> <value>
 *  ret void
 */
public class IrRetInstr extends IrInstruction {
    private final boolean retVoid;

    public IrRetInstr(IrValue retValue) {
        super(IrVoidType.VOID, "ret", IrInstrType.RET);
        if (retValue != null) {
            this.retVoid = false;
            addOperand(retValue);
        } else {
            this.retVoid = true;
        }
    }

    public IrValue getRetValue() {
        if (!retVoid) {
            return getOperand(0);
        }
        return null;
    }

    @Override
    public String irOutput() {
        if (retVoid) {
            return "ret void\n";
        } else {
            return "ret " + getRetValue().getType().irOutput() + " " +
                    getRetValue().getName() + "\n";
        }
    }

    @Override
    public void genAsm() {
        if (MipsBuilder.getInstance().getCurFunc().getName().equals("@main")) {
            // exit
            new MipsComment("exit");
            new MipsLiInstr(Register.V0, 10);
            new MipsSyscallInstr();
            return;
        }

        super.genAsm();

        IrValue retValue = getRetValue();
        if (!retVoid) { // prepare v0
            if (retValue instanceof IrConstInt constInt) {
                new MipsLiInstr(Register.V0, constInt.getValue());
            } else if (MipsBuilder.getInstance().getRegFor(retValue) != null) { // have reg
                new MipsMoveInstr(Register.V0, MipsBuilder.getInstance().getRegFor(retValue));
            } else { // load-from-stack
                new MipsLoadInstr(MipsLoadInstr.Op.lw, Register.V0, Register.SP, MipsBuilder.getInstance().getOffsetOf(retValue));
            }
        }

        new MipsJumpInstr(MipsJumpInstr.Op.jr, Register.RA);
    }
}
