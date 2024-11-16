package middle.llvm_ir.utils;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrVoidType;

public class IrUninitValue extends IrValue {
    public IrUninitValue() {
        super(IrVoidType.VOID, "0");
    }

    @Override
    public String irOutput() {
        return "uninitialized";
    }
}
