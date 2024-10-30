package middle.llvm_ir;

import middle.llvm_ir.constant.IrConstant;
import middle.llvm_ir.type.IrType;

public class IrGlobalVar extends IrUser {
    private final IrConstant initVal;
    public IrGlobalVar(IrType type, String name, IrConstant initVal) {
        super(type, name);
        this.initVal = initVal;
    }

    @Override
    public String irOutput() {
        return getName() + " = dso_local global " +
                initVal.irOutput() + "\n";
    }
}
