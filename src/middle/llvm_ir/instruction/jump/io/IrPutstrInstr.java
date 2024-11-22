package middle.llvm_ir.instruction.jump.io;

import backend.mips.Register;
import backend.mips.assembly.instruction.MipsSyscallInstr;
import backend.mips.assembly.instruction.extended.MipsLaInstr;
import backend.mips.assembly.instruction.extended.MipsLiInstr;
import middle.llvm_ir.utils.IrStrLiteral;
import middle.llvm_ir.type.IrPointerType;
import middle.llvm_ir.type.IrVoidType;

public class IrPutstrInstr extends IrIOInstr {
    public IrPutstrInstr(IrStrLiteral strLiteral) {
        super(IrVoidType.VOID, "putstr");
        addOperand(strLiteral);
    }

    public IrStrLiteral getStrLiteral() {
        return (IrStrLiteral) getOperand(0);
    }

    public static String getDeclare() {
        return "declare void @putstr(i8*)\n";
    }

    @Override
    public String irOutput() {
        IrStrLiteral strLiteral = getStrLiteral();
        IrPointerType strPtrType = (IrPointerType) strLiteral.getType();

        return "call void @putstr(i8* getelementptr inbounds (" +
                strPtrType.getTargetType().irOutput() + ", " +
                strPtrType.irOutput() + " " +
                strLiteral.getName() + ", i64 0, i64 0))\n";
    }

    @Override
    public void genAsm() {
        super.genAsm();

        new MipsLaInstr(Register.A0, getStrLiteral().getName().substring(2));
        new MipsLiInstr(Register.V0, 4);
        new MipsSyscallInstr();
    }
}
