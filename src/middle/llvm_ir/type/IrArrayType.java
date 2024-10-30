package middle.llvm_ir.type;

public class IrArrayType extends IrType {
    private final IrType eleType;
    private final int eleNum;

    public IrArrayType(IrType eleType, int eleNum) {
        this.eleType = eleType;
        this.eleNum = eleNum;
    }

    public IrType getEleType() {
        return eleType;
    }

    @Override
    public String irOutput() {
        return "[" + eleNum + " x " + eleType.irOutput() + "]";
    }
}
