package middle.llvm_ir.instruction.jump.br;

import middle.llvm_ir.IrBasicBlock;

/**
 * br label <dest>
 */
public class IrDirtBrInstr extends IrBrInstr {
    public IrDirtBrInstr(String name, IrBasicBlock destBlock) {
        super(name);
        addOperand(destBlock);
    }

    public IrBasicBlock getDestBlock() {
        return (IrBasicBlock) getOperand(0);
    }

    @Override
    public String irOutput() {
        return "br label %" + getDestBlock().getName() + "\n";
    }
}
