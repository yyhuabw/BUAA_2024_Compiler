package middle.llvm_ir.utils;

import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrUser;
import middle.llvm_ir.utils.constant.IrConstant;
import middle.llvm_ir.type.IrType;

public class IrGlobalVar extends IrUser {
    private final IrConstant initVal;
    public IrGlobalVar(IrType type, String name, IrConstant initVal) {
        super(type, name);
        this.initVal = initVal;

        IrBuilder.getInstance().addGlobalVar(this);
    }

    @Override
    public String irOutput() {
        return getName() + " = dso_local global " +
                initVal.irOutput() + "\n";
    }
}
