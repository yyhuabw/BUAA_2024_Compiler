package middle.llvm_ir.instruction.jump.io;

import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrType;

public class IrIOInstr extends IrInstruction {
    public IrIOInstr(IrType type, String name) {
        super(type, name, IrInstrType.IO);
    }

    public String getDeclare() {
        return "";
    }
}
