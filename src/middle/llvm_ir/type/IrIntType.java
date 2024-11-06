package middle.llvm_ir.type;

public class IrIntType extends IrType {
    public final static IrIntType INT1 = new IrIntType(1);
    public final static IrIntType INT8 = new IrIntType(8);
    public final static IrIntType INT32 = new IrIntType(32);

    private final int bitWidth;

    private IrIntType(int bitWidth) {
        this.bitWidth = bitWidth;
    }

    @Override
    public String irOutput() {
        if (bitWidth == 1) {
            return "i1";
        } else if (bitWidth == 8) {
            return "i8";
        } else if (bitWidth == 32) {
            return "i32";
        }
        return "ERROR";
    }
}
