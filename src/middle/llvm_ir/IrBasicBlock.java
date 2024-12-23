package middle.llvm_ir;

import backend.mips.assembly.instruction.MipsLabel;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrLabelType;

import java.util.LinkedList;

public class IrBasicBlock extends IrUser {
    private final LinkedList<IrInstruction> instrList;
    private IrFunction parentFunc = null;

    /**
     * @param name: the label of basicBlock
     */
    public IrBasicBlock(String name) {
        super(IrLabelType.LABEL, name);
        this.instrList = new LinkedList<>();

        if (IrBuilder.getInstance().isAutoInsertMode()) {
            IrBuilder.getInstance().curFuncAddBlock(this);
        }
    }

    public void addInstr(IrInstruction instr) {
        instrList.add(instr);
    }

    public void setParentFunc(IrFunction parentFunc) {
        this.parentFunc = parentFunc;
    }


    public LinkedList<IrInstruction> getInstrList() {
        return instrList;
    }

    @Override
    public String irOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(":\n");
        for (IrInstruction instr : instrList) {
            sb.append("    ").append(instr.irOutput());
        }
        return sb.toString();
    }

    @Override
    public void genAsm() {
        new MipsLabel(getName());
        for (IrInstruction instruction : instrList) {
            instruction.genAsm();
        }
    }
}
