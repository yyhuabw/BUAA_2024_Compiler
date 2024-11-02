package middle.llvm_ir.utils;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrArrayType;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.type.IrPointerType;

public class IrStrLiteral extends IrValue {
    private final String content;

    /**
     * string pointer
     * type -> [(len+1) x i8]*
     */
    public IrStrLiteral(String name, String content) {
        super(new IrPointerType(new IrArrayType(IrIntType.INT8, content.length() + 1)), name);
        this.content = content;
    }

    @Override
    public String irOutput() {
        return getName() + " = private unnamed_addr constant " +
                ((IrPointerType) getType()).getTargetType().irOutput() + " c\"" +
                content + "\\00\", align 1\n";
    }
}
