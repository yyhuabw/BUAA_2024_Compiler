package middle.llvm_ir.instruction.memory;

import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrPointerType;
import middle.llvm_ir.type.IrType;

public class IrAllocaInstr extends IrInstruction {
    private final IrType targetType; // target* (point to target)

    public IrAllocaInstr(String name, IrType targetType) {
        super(new IrPointerType(targetType), name, IrInstrType.ALLOCA);
        this.targetType = targetType;
    }

    public IrType getTargetType() {
        return targetType;
    }

    @Override
    public String irOutput() {
        return getName() + " = alloca " + targetType.irOutput() + "\n";
    }
}
