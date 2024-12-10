package middle.llvm_ir.instruction.jump.br;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.jump.MipsBranchInstr;
import backend.mips.assembly.instruction.jump.MipsJumpInstr;
import backend.mips.assembly.instruction.memory.MipsLoadInstr;
import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrValue;

/**
 * br i1 <cond>, label <iftrue>, label <iffalse>
 */
public class IrCondBrInstr extends IrBrInstr {
    public IrCondBrInstr(IrValue cond,
                         IrBasicBlock ifTrueBlock, IrBasicBlock ifFalseBlock) {
        super();
        addOperand(cond);
        addOperand(ifTrueBlock);
        addOperand(ifFalseBlock);
    }

    public IrValue getCond() {
        return getOperand(0);
    }

    public IrBasicBlock getIfTrueBlock() {
        return (IrBasicBlock) getOperand(1);
    }

    public IrBasicBlock getIfFalseBlock() {
        return (IrBasicBlock) getOperand(2);
    }

    public void setIfTrueBlock(IrBasicBlock ifTrueBlock) {
        setOperand(1, ifTrueBlock);
    }

    public void setIfFalseBlock(IrBasicBlock ifFalseBlock) {
        setOperand(2, ifFalseBlock);
    }

    @Override
    public String irOutput() {
        return "br i1 " +
                getCond().getName() +
                ", label %" + getIfTrueBlock().getName() +
                ", label %" + getIfFalseBlock().getName() + "\n";
    }

    @Override
    public void genAsm() {
        super.genAsm();

        IrValue cond = getCond();

        // get cond's reg
        Register reg = MipsBuilder.getInstance().getRegFor(cond);
        if (reg == null) {
            reg = Register.K0;
            new MipsLoadInstr(MipsLoadInstr.Op.lw, reg, Register.SP, MipsBuilder.getInstance().getOrSetOffsetOf(cond));
        }

        // cond == 1 <-> true
        new MipsBranchInstr(MipsBranchInstr.Op.bne, reg, Register.ZERO, getIfTrueBlock().getName());
        // cond == 0 <-> false
        new MipsJumpInstr(MipsJumpInstr.Op.j, getIfFalseBlock().getName());
    }
}
