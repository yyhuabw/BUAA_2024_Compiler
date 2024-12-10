package middle.llvm_ir.instruction.memory;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.extended.MipsLaInstr;
import backend.mips.assembly.instruction.memory.MipsLoadInstr;
import backend.mips.assembly.instruction.memory.MipsStoreInstr;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrPointerType;
import middle.llvm_ir.utils.IrGlobalVar;

public class IrLoadInstr extends IrInstruction {
    public IrLoadInstr(String name, IrValue pointer) {
        super(((IrPointerType) pointer.getType()).getTargetType(), name, IrInstrType.LOAD);
        addOperand(pointer);
    }

    public IrValue getPointer() {
        return getOperand(0);
    }

    @Override
    public boolean canBeUsed() {
        return true;
    }

    @Override
    public String irOutput() {
        return getName() + " = load " + getType().irOutput() +
                ", " + getPointer().getType().irOutput() +
                " " + getPointer().getName() + "\n";
    }

    @Override
    public void genAsm() {
        super.genAsm();

        Register pointerReg = pointer2reg(getPointer());

        Register result = MipsBuilder.getInstance().getRegFor(this);
        if (result == null) {
            result = Register.K0;
        }

        // mem[pointer] -> result
        new MipsLoadInstr(MipsLoadInstr.Op.lw, result, pointerReg, 0);

        // result-to-stack
        if (result == Register.K0) {
            MipsBuilder.getInstance().downwardCurOffset(4);
            int curOffset = MipsBuilder.getInstance().getCurStackOffset();
            MipsBuilder.getInstance().addValueMapping(this, curOffset);
            new MipsStoreInstr(MipsStoreInstr.Op.sw, result, Register.SP, curOffset);
        }
    }

    private Register pointer2reg(IrValue pointer) {
        if (pointer instanceof IrGlobalVar) {
            new MipsLaInstr(Register.K0, pointer.getName().substring(1));
            return Register.K0;
        }

        Register reg = MipsBuilder.getInstance().getRegFor(pointer);
        if (reg != null) {
            return reg;
        }

        // load-from-stack
        new MipsLoadInstr(MipsLoadInstr.Op.lw, Register.K0, Register.SP, MipsBuilder.getInstance().getOrSetOffsetOf(pointer));
        return Register.K0;
    }
}
