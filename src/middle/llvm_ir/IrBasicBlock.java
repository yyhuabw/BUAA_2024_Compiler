package middle.llvm_ir;

import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrLabelType;

import java.util.ArrayList;

public class IrBasicBlock extends IrUser {
    private final ArrayList<IrInstruction> instrList;
    private IrFunction parentFunc = null;

    /**
     * @param name: the label of basicBlock
     */
    public IrBasicBlock(String name) {
        super(IrLabelType.LABEL, name);
        this.instrList = new ArrayList<>();
    }

    public void addInstr(IrInstruction instr) {
        instrList.add(instr);
    }

    public void setParentFunc(IrFunction parentFunc) {
        this.parentFunc = parentFunc;
    }

    @Override
    public String irOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(":\n");
        for (IrInstruction instr : instrList) {
            sb.append(instr.irOutput());
        }
        return sb.toString();
    }
}
