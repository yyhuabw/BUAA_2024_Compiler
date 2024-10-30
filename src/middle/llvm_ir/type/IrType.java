package middle.llvm_ir.type;

/**
 * The return valueType of an instruction
 */
public class IrType {
    public boolean isArray() {
        return this instanceof IrArrayType;
    }

    public String irOutput() {
        return "";
    }
}
