package middle.llvm_ir.instruction.type_change;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrType;

public class IrZextInstr extends IrInstruction {
    public IrZextInstr(IrType finalType, String name, IrValue originValue) {
        super(finalType, name, IrInstrType.ZEXT);
        addOperand(originValue);
    }

    public IrValue getOriginValue() {
        return getOperand(0);
    }

    @Override
    public String irOutput() {
        return getName() + " = zext " +
                getOriginValue().getType().irOutput() + " " +
                getOriginValue().getName() + " to " +
                getType().irOutput() + "\n";
    }
}
