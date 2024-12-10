package middle.llvm_ir.instruction.memory;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.extended.MipsLaInstr;
import backend.mips.assembly.instruction.extended.MipsLiInstr;
import backend.mips.assembly.instruction.memory.MipsLoadInstr;
import backend.mips.assembly.instruction.memory.MipsStoreInstr;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrVoidType;
import middle.llvm_ir.utils.IrGlobalVar;
import middle.llvm_ir.utils.constant.IrConstInt;

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

    @Override
    public void genAsm() {
        super.genAsm();

        Register valueReg = value2reg(getValue());
        Register pointerReg = pointer2reg(getPointer());

        // sw
        new MipsStoreInstr(MipsStoreInstr.Op.sw, valueReg, pointerReg, 0);
    }

    // k0 -> tmp
    private Register value2reg(IrValue value) {
        if (value instanceof IrConstInt constInt) {
            new MipsLiInstr(Register.K0, constInt.getValue());
            return Register.K0;
        }

        Register reg = MipsBuilder.getInstance().getRegFor(value);
        if (reg != null) {
            return reg;
        }

        // load-from-stack
        new MipsLoadInstr(MipsLoadInstr.Op.lw, Register.K0, Register.SP, MipsBuilder.getInstance().getOrSetOffsetOf(value));
        return Register.K0;
    }

    // k1 -> tmp
    private Register pointer2reg(IrValue pointer) {
        if (pointer instanceof IrGlobalVar) {
            new MipsLaInstr(Register.K1, pointer.getName().substring(1));
            return Register.K1;
        }

        Register reg = MipsBuilder.getInstance().getRegFor(pointer);
        if (reg != null) {
            return reg;
        }

        // load-from-stack
        new MipsLoadInstr(MipsLoadInstr.Op.lw, Register.K1, Register.SP, MipsBuilder.getInstance().getOrSetOffsetOf(pointer));
        return Register.K1;
    }
}
