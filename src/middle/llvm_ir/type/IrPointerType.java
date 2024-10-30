package middle.llvm_ir.type;

public class IrPointerType extends IrType {
    private final IrType targetType;

    public IrPointerType(IrType targetType) {
        this.targetType = targetType;
    }

    public IrType getTargetType() {
        return targetType;
    }

    @Override
    public String irOutput() {
        return targetType.irOutput() + "*";
    }
}
