package middle.llvm_ir.instruction.jump.call;

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
}
