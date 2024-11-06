package middle.llvm_ir.instruction.memory;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrVoidType;

/**
 * store value to pointer
 */
public class IrStoreInstr extends IrInstruction {
    public IrStoreInstr(IrValue value, IrValue pointer) {
        super(IrVoidType.VOID, "store", IrInstrType.STORE);
        addOperand(value);
        addOperand(pointer);
    }

    public IrValue getValue() {
        return getOperand(0);
    }

    public IrValue getPointer() {
        return getOperand(1);
    }

    @Override
    public String irOutput() {
        return "store " + getValue().getType().irOutput() + " " +
                getValue().getName() + ", " +
                getPointer().getType().irOutput() + " " +
                getPointer().getName() + "\n";
    }
}
