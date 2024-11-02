package middle.llvm_ir.instruction.jump.io;

import middle.llvm_ir.utils.IrStrLiteral;
import middle.llvm_ir.type.IrPointerType;
import middle.llvm_ir.type.IrVoidType;

public class IrPutstrInstr extends IrIOInstr {
    public IrPutstrInstr(String name, IrStrLiteral strLiteral) {
        super(IrVoidType.VOID, name);
        addOperand(strLiteral);
    }

    public IrStrLiteral getStrLiteral() {
        return (IrStrLiteral) getOperand(0);
    }

    @Override
    public String getDeclare() {
        return "declare void @putstr(i8*)\n";
    }

    @Override
    public String irOutput() {
        IrStrLiteral strLiteral = getStrLiteral();
        IrPointerType strPtrType = (IrPointerType) strLiteral.getType();

        return "call void @putstr(i8* getelementptr inbounds (" +
                strPtrType.getTargetType().irOutput() + ", " +
                strPtrType + " " +
                strLiteral.getName() + ", i64 0, i64 0))\n";
    }
}
