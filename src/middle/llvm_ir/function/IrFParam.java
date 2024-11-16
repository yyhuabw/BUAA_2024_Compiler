package middle.llvm_ir.function;

import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrType;

/**
 * the param is func_formal_param
 */
public class IrFParam extends IrValue {
    private IrFunction parentFunc;

    public IrFParam(IrType type, String name) {
        super(type, name);

        if (IrBuilder.getInstance().isAutoInsertMode()) {
            IrBuilder.getInstance().curFuncAddParam(this);
        }
    }

    public void setParentFunc(IrFunction function) {
        this.parentFunc = function;
    }

    @Override
    public String irOutput() {
        return getType().irOutput() + " " + getName();
    }
}
