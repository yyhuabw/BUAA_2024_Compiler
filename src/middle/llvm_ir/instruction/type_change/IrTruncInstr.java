package middle.llvm_ir.instruction.type_change;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrType;

public class IrTruncInstr extends IrInstruction {
    private final IrType finalType;

    public IrTruncInstr(IrType finalType, String name, IrValue originValue) {
        super(finalType, name, IrInstrType.TRUNC);
        this.finalType = finalType;
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
                finalType.irOutput() + "\n";
    }
}
