package middle.llvm_ir.instruction.jump.io;

import middle.llvm_ir.type.IrIntType;

public class IrGetcharInstr extends IrIOInstr {
    public IrGetcharInstr(String name) {
        super(IrIntType.INT32, name);
    }

    public static String getDeclare() {
        return "declare i32 @getchar()\n";
    }

    @Override
    public String irOutput() {
        return getName() + " = call i32 @getchar()\n";
    }
}
