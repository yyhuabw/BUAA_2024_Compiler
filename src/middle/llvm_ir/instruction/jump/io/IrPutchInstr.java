package middle.llvm_ir.instruction.jump.io;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrVoidType;

public class IrPutchInstr extends IrIOInstr {
    public IrPutchInstr(IrValue value) {
        super(IrVoidType.VOID, "putch");
        addOperand(value);
    }

    public IrValue getValue() {
        return getOperand(0);
    }

    public static String getDeclare() {
        return "declare void @putch(i32)\n";
    }

    @Override
    public String irOutput() {
        return "call void @putch(i32 " +
                getValue().getName() + ")\n";
    }
}
