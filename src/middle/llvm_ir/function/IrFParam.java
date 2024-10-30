package middle.llvm_ir.function;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrType;

/**
 * the param is func_formal_param
 */
public class IrFParam extends IrValue {
    public IrFParam(IrType type, String name) {
        super(type, name);
    }

    @Override
    public String irOutput() {
        return getType().irOutput() + " " + getName();
    }
}
