package middle.llvm_ir.instruction.jump.io;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrVoidType;

public class IrPutintInstr extends IrIOInstr {
    public IrPutintInstr(IrValue value) {
        super(IrVoidType.VOID, "putint");
        addOperand(value);
    }

    public IrValue getValue() {
        return getOperand(0);
    }

    public static String getDeclare() {
        return "declare void @putint(i32)\n";
    }

    @Override
    public String irOutput() {
        return "call void @putint(i32 " +
                getValue().getName() + ")\n";
    }
}
