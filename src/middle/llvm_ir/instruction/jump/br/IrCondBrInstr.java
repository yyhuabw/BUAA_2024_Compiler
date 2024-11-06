package middle.llvm_ir.instruction.jump.br;

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

    @Override
    public String irOutput() {
        return "br i1 " +
                getCond().getName() +
                ", label %" + getIfTrueBlock().getName() +
                ", label %" + getIfFalseBlock().getName() + "\n";
    }
}
