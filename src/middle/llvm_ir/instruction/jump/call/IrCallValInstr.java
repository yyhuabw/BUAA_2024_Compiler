package middle.llvm_ir.instruction.jump.call;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.extended.MipsMoveInstr;
import backend.mips.assembly.instruction.memory.MipsStoreInstr;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.function.IrFunction;

import java.util.ArrayList;

/**
 * <result> = call [ret attrs] <ty> <name>(<...args>)
 */
public class IrCallValInstr extends IrCallInstr {
    public IrCallValInstr(String name, IrFunction function, ArrayList<IrValue> params) {
        super(name, function, params);
    }

    @Override
    public boolean canBeUsed() {
        return true;
    }

    @Override
    public String irOutput() {
        return getName() + " = call " +
                getType().irOutput() + " " +
                getFunction().getName() + "(" +
                String.join(", ", getParamsInfo()) + ")\n";
    }

    @Override
    public void genAsm() {
        super.genAsm();

        Register reg = MipsBuilder.getInstance().getRegFor(this);
        if (reg != null) {
            new MipsMoveInstr(reg, Register.V0);
        } else {
            MipsBuilder.getInstance().downwardCurOffset(4);
            int curOffset = MipsBuilder.getInstance().getCurStackOffset();
            MipsBuilder.getInstance().addValueMapping(this, curOffset);
            new MipsStoreInstr(MipsStoreInstr.Op.sw, Register.V0, Register.SP, curOffset);
        }
    }
}
