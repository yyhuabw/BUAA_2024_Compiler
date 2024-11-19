package middle.llvm_ir.instruction.memory;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.alu.MipsRIAluInstr;
import backend.mips.assembly.instruction.memory.MipsStoreInstr;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrArrayType;
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
    public boolean canBeUsed() {
        return true;
    }

    @Override
    public String irOutput() {
        return getName() + " = alloca " + targetType.irOutput() + "\n";
    }

    @Override
    public void genAsm() {
        super.genAsm();

        // allocate space in stack
        if (targetType instanceof IrArrayType arrayType) { // array
            MipsBuilder.getInstance().downwardCurOffset(4 * arrayType.getEleNum());
        } else { // non array
            MipsBuilder.getInstance().downwardCurOffset(4);
        }

        // address to-reg or store-to-stack
        Register reg = MipsBuilder.getInstance().getRegFor(this);
        int curOffset = MipsBuilder.getInstance().getCurStackOffset();
        if (reg != null) { // have reg
            new MipsRIAluInstr(MipsRIAluInstr.Op.addiu, reg, Register.SP, curOffset);
        } else { // need store-to-stack
            // temporarily stored in k0
            new MipsRIAluInstr(MipsRIAluInstr.Op.addiu, Register.K0, Register.SP, curOffset);

            // store addr to stack
            MipsBuilder.getInstance().downwardCurOffset(4);
            curOffset = MipsBuilder.getInstance().getCurStackOffset();
            MipsBuilder.getInstance().addValueMapping(this, curOffset);
            new MipsStoreInstr(MipsStoreInstr.Op.sw, Register.K0, Register.SP, curOffset);
        }
    }
}
