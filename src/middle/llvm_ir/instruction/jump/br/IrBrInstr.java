package middle.llvm_ir.instruction.jump.br;

import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrVoidType;

/**
 * br i1 <cond>, label <iftrue>, label <iffalse>
 * br label <dest>
 */
public class IrBrInstr extends IrInstruction {
    public IrBrInstr() {
        super(IrVoidType.VOID, "br", IrInstrType.BR);
    }
}
