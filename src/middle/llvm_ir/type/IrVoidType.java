package middle.llvm_ir.type;

public class IrVoidType extends IrType {
    public final static IrVoidType VOID = new IrVoidType();

    private IrVoidType() {}

    @Override
    public String irOutput() {
        return "void";
    }
}
