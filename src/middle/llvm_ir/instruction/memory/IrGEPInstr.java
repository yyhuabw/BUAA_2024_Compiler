package middle.llvm_ir.instruction.memory;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrPointerType;
import middle.llvm_ir.type.IrType;

/**
 * <result> = getelementptr inbounds <ty>, <ty>* <ptrval>{, [inrange] <ty> <idx>}*
 */
public class IrGEPInstr extends IrInstruction {
    public IrGEPInstr(IrPointerType type, String name, IrValue ptrValue, IrValue index) {
        super(type, name, IrInstrType.GEP);
        addOperand(ptrValue);
        addOperand(index);
    }

    public IrValue getPtrValue() {
        return getOperand(0);
    }

    public IrValue getIndex() {
        return getOperand(1);
    }

    @Override
    public String irOutput() {
        IrValue ptrValue = getPtrValue();
        IrValue index = getIndex();
        IrPointerType ptrType = (IrPointerType) ptrValue.getType();
        IrType trgtType = ptrType.getTargetType();

        if (trgtType.isArray()) {
            return getName() + " = getelementptr inbounds " +
                    trgtType.irOutput() + ", " +
                    ptrType.irOutput() + " " +
                    ptrValue.getName() + ", i32 0, " +
                    index.getType().irOutput() + " " +
                    index.getName() + "\n"; // the type of index should be i32
        } else {
            return getName() + " = getelementptr inbounds " +
                    trgtType.irOutput() + ", " +
                    ptrType.irOutput() + " " +
                    ptrValue.getName() + ", " +
                    index.getType().irOutput() + " " +
                    index.getName() + "\n"; // the type of index should be i32
        }
    }
}
