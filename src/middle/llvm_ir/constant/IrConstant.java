package middle.llvm_ir.constant;

import middle.llvm_ir.IrNode;
import middle.llvm_ir.type.IrType;

public class IrConstant implements IrNode {
    private final IrType type;

    public IrConstant(IrType type) {
        this.type = type;
    }

    public IrType getType() {
        return type;
    }

    @Override
    public String irOutput() {
        return "";
    }
}
