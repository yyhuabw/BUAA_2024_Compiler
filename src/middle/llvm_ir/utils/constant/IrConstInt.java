package middle.llvm_ir.utils.constant;

import middle.llvm_ir.type.IrType;

public class IrConstInt extends IrConstant {
    private final int value;

    public IrConstInt(IrType type, int value) {
        super(type, String.valueOf(value));
        this.value = value;
    }

    @Override
    public String irOutput() {
        return getType().irOutput() + " " + value;
    }
}
