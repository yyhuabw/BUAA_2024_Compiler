package middle.llvm_ir.instruction.jump.io;

import middle.llvm_ir.type.IrIntType;

public class IrGetintInstr extends IrIOInstr {
    public IrGetintInstr(String name) {
        super(IrIntType.INT32, name);
    }

    @Override
    public String getDeclare() {
        return "declare i32 @getint()\n";
    }

    @Override
    public String irOutput() {
        return getName() + " = call i32 @getint()\n";
    }
}
