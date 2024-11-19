package middle.llvm_ir.instruction;

import backend.mips.assembly.instruction.MipsComment;
import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrUser;
import middle.llvm_ir.type.IrType;

public class IrInstruction extends IrUser {
    private IrBasicBlock parentBlock;

    private final IrInstrType instrType;

    public IrInstruction(IrType type, String name, IrInstrType instrType) {
        super(type, name);
        this.instrType = instrType;

        // add the instruction to the block when create it
        if (IrBuilder.getInstance().isAutoInsertMode()) {
            IrBuilder.getInstance().curBlockAddInstr(this);
        }
    }

    public void setParentBlock(IrBasicBlock block) {
        this.parentBlock = block;
    }

    public boolean canBeUsed() {
        return false;
    }

    public IrBasicBlock getParentBlock() {
        return parentBlock;
    }

    @Override
    public void genAsm() {
        new MipsComment(this.irOutput().substring(0, this.irOutput().length() - 1));
    }
}
