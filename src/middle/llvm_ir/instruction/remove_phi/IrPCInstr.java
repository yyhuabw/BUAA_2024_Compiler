package middle.llvm_ir.instruction.remove_phi;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrVoidType;

import java.util.ArrayList;

// parallel copy instruction
public class IrPCInstr extends IrInstruction {
    private final ArrayList<IrValue> dstList;
    private final ArrayList<IrValue> srcList;

    public IrPCInstr(String name) {
        super(IrVoidType.VOID, name, IrInstrType.PC);
        this.dstList = new ArrayList<>();
        this.srcList = new ArrayList<>();
    }

    public void addCopy(IrValue dst, IrValue src) {
        dstList.add(dst);
        srcList.add(src);
    }

    public ArrayList<IrValue> getDstList() {
        return dstList;
    }

    public ArrayList<IrValue> getSrcList() {
        return srcList;
    }
}
