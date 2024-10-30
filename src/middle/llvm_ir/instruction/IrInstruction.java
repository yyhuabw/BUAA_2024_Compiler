package middle.llvm_ir.instruction;

import middle.llvm_ir.IrUser;
import middle.llvm_ir.type.IrType;

public class IrInstruction extends IrUser {
    private final IrInstrType instrType;

    public IrInstruction(IrType type, String name, IrInstrType instrType) {
        super(type, name);
        this.instrType = instrType;
    }

    public IrInstrType getInstrType() {
        return instrType;
    }
}
