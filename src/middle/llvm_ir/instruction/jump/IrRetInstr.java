package middle.llvm_ir.instruction.jump;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrVoidType;

/**
 *  ret <type> <value>
 *  ret void
 */
public class IrRetInstr extends IrInstruction {
    private final boolean retVoid;

    public IrRetInstr(String name, IrValue retValue) {
        super(IrVoidType.VOID, name, IrInstrType.RET);
        if (retValue != null) {
            this.retVoid = false;
            addOperand(retValue);
        } else {
            this.retVoid = true;
        }
    }

    public IrValue getRetValue() {
        if (!retVoid) {
            return getOperand(0);
        }
        return null;
    }

    @Override
    public String irOutput() {
        if (retVoid) {
            return "ret void\n";
        } else {
            return "ret " + getRetValue().getType().irOutput() + " " +
                    getRetValue().getName() + "\n";
        }
    }
}
