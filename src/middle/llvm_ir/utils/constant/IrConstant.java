package middle.llvm_ir.utils.constant;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrType;

public class IrConstant extends IrValue {
    public IrConstant(IrType type, String name) {
        super(type, name);
    }
}
