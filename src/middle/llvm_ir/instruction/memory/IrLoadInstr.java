package middle.llvm_ir.instruction.memory;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrPointerType;

public class IrLoadInstr extends IrInstruction {
    public IrLoadInstr(String name, IrValue pointer) {
        super(((IrPointerType) pointer.getType()).getTargetType(), name, IrInstrType.LOAD);
        addOperand(pointer);
    }

    public IrValue getPointer() {
        return getOperand(0);
    }

    @Override
    public String irOutput() {
        return getName() + " = load " + getType().irOutput() +
                ", " + getPointer().getType().irOutput() +
                " " + getPointer().getName() + "\n";
    }
}
