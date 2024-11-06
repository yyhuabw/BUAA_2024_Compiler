package middle.llvm_ir.utils;

import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrVoidType;

public class IrForLoop extends IrValue {
    private final IrBasicBlock stepBlock;
    private final IrBasicBlock followBlock;

    public IrForLoop(IrBasicBlock stepBlock, IrBasicBlock followBlock) {
        super(IrVoidType.VOID, "for loop");
        this.stepBlock = stepBlock;
        this.followBlock = followBlock;
    }

    public IrBasicBlock getStepBlock() {
        return stepBlock;
    }

    public IrBasicBlock getFollowBlock() {
        return followBlock;
    }
}
