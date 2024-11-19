package middle.llvm_ir.utils;

import backend.mips.assembly.globalDecl.byteAsm.MipsByteArrayAsm;
import backend.mips.assembly.globalDecl.byteAsm.MipsByteCharAsm;
import backend.mips.assembly.globalDecl.wordAsm.MipsWordArrayAsm;
import backend.mips.assembly.globalDecl.wordAsm.MipsWordIntAsm;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrUser;
import middle.llvm_ir.type.IrArrayType;
import middle.llvm_ir.utils.constant.IrConstArray;
import middle.llvm_ir.utils.constant.IrConstInt;
import middle.llvm_ir.utils.constant.IrConstant;
import middle.llvm_ir.type.IrType;

import java.util.ArrayList;

public class IrGlobalVar extends IrUser {
    private final IrConstant initVal;
    public IrGlobalVar(IrType type, String name, IrConstant initVal) {
        super(type, name);
        this.initVal = initVal;

        if (IrBuilder.getInstance().isAutoInsertMode()) {
            IrBuilder.getInstance().addGlobalVar(this);
        }
    }

    @Override
    public String irOutput() {
        return getName() + " = dso_local global " +
                initVal.irOutput() + "\n";
    }

    @Override
    public void genAsm() {
        String name = getName().substring(1);
        if (initVal instanceof IrConstInt constInt) { // int | char
            if (constInt.getType().isINT32()) { // int
                new MipsWordIntAsm(name, constInt.getValue());
            } else { // char
                new MipsByteCharAsm(name, constInt.getValue());
            }
        } else if (initVal instanceof IrConstArray constArray) { // array
            IrArrayType arrayType = (IrArrayType) constArray.getType();
            int size = arrayType.getEleNum();
            if (arrayType.getEleType().isINT32()) { // int[]
                if (constArray.needZeroInit()) {
                    new MipsWordArrayAsm(name, new ArrayList<>(), size);
                } else {
                    new MipsWordArrayAsm(name, constArray.getAllValue(), size);
                }
            } else { // char[]
                if (constArray.needZeroInit()) {
                    new MipsByteArrayAsm(name, new ArrayList<>(), size);
                } else {
                    new MipsByteArrayAsm(name, constArray.getAllValue(), size);
                }
            }
        }
    }
}
