package middle.llvm_ir.type;

/**
 * The return valueType of an instruction
 */
public class IrType {
    public boolean isINT32() {
        return this == IrIntType.INT32;
    }

    public boolean isVoid() {
        return this == IrVoidType.VOID;
    }

    public boolean isArray() {
        return this instanceof IrArrayType;
    }

    public String irOutput() {
        return "";
    }
}
