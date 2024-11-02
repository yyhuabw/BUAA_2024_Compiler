package middle.llvm_ir.instruction.type_change;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrType;

public class IrTruncInstr extends IrInstruction {
    public IrTruncInstr(IrType finalType, String name, IrValue originValue) {
        super(finalType, name, IrInstrType.TRUNC);
        addOperand(originValue);
    }

    public IrValue getOriginValue() {
        return getOperand(0);
    }

    @Override
    public String irOutput() {
        return getName() + " = trunc " +
                getOriginValue().getType().irOutput() + " " +
                getOriginValue().getName() + " to " +
                getType().irOutput() + "\n";
    }
}
