package middle.llvm_ir.instruction.remove_phi;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.extended.MipsLiInstr;
import backend.mips.assembly.instruction.extended.MipsMoveInstr;
import backend.mips.assembly.instruction.memory.MipsLoadInstr;
import backend.mips.assembly.instruction.memory.MipsStoreInstr;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrVoidType;
import middle.llvm_ir.utils.constant.IrConstInt;

// for phi-pc-move
public class IrMoveInstr extends IrInstruction {
    public IrMoveInstr(String name, IrValue dst, IrValue src) {
        super(IrVoidType.VOID, name, IrInstrType.MOVE);
        addOperand(dst);
        addOperand(src);
    }

    public IrValue getDst() {
        return getOperand(0);
    }

    public IrValue getSrc() {
        return getOperand(1);
    }

    public void setDst(IrValue dst) {
        setOperand(0, dst);
    }

    public void setSrc(IrValue src) {
        setOperand(1, src);
    }

    @Override
    public String irOutput() {
        return "move " + getDst().getType().irOutput() + " " +
                getDst().getName() + ", " +
                getSrc().getName();
    }

    @Override
    public void genAsm() {
        super.genAsm();

        IrValue dst = getDst();
        IrValue src = getSrc();
        Register dstReg = MipsBuilder.getInstance().getRegFor(dst);

        // dstReg == srcReg
        if (dstReg != null && MipsBuilder.getInstance().getRegFor(src) == dstReg) {
            return;
        }

        if (dstReg == null) {
            dstReg = Register.K0;
        }

        // scrValue -> dstReg
        if (src instanceof IrConstInt constInt) { // imm
            new MipsLiInstr(dstReg, constInt.getValue());
        } else if (MipsBuilder.getInstance().getRegFor(src) != null) { // reg
            Register srcReg = MipsBuilder.getInstance().getRegFor(src);
            new MipsMoveInstr(dstReg, srcReg);
        } else { // memory
            new MipsLoadInstr(MipsLoadInstr.Op.lw, dstReg, Register.SP, MipsBuilder.getInstance().getOrSetOffsetOf(src));
        }

        // store-to-stack
        if (dstReg == Register.K0) {
            new MipsStoreInstr(MipsStoreInstr.Op.sw, dstReg, Register.SP, MipsBuilder.getInstance().getOrSetOffsetOf(dst));
        }
    }
}
