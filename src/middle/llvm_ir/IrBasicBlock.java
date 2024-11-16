package middle.llvm_ir;

import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.type.IrLabelType;

import java.util.ArrayList;
import java.util.LinkedList;

public class IrBasicBlock extends IrUser {
    private final LinkedList<IrInstruction> instrList;
    private IrFunction parentFunc = null;

    private boolean isDeleted = false;

    // control flow graph
    private ArrayList<IrBasicBlock> prevBlocks;
    private ArrayList<IrBasicBlock> nextBlocks;
    // blocks which are dominated by this
    private ArrayList<IrBasicBlock> domList;
    // this's immediate dominator
    private IrBasicBlock idomor;
    // this's immediate dominateds
    private ArrayList<IrBasicBlock> idomeds;
    // dominance frontier
    private ArrayList<IrBasicBlock> DF;

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

    public IrFunction getParentFunc() {
        return parentFunc;
    }

    public LinkedList<IrInstruction> getInstrList() {
        return instrList;
    }

    public IrInstruction getFirstInstr() {
        return instrList.getFirst();
    }

    public IrInstruction getLastInstr() {
        return instrList.getLast();
    }

    public void markDeleted() {
        this.isDeleted = true;
    }

    public boolean alive() {
        return !isDeleted;
    }

    public void setPrevBlocks(ArrayList<IrBasicBlock> prevBlocks) {
        this.prevBlocks = prevBlocks;
    }

    public void setNextBlocks(ArrayList<IrBasicBlock> nextBlocks) {
        this.nextBlocks = nextBlocks;
    }

    public void setDomList(ArrayList<IrBasicBlock> domList) {
        this.domList = domList;
    }

    public void setIdomor(IrBasicBlock idomor) {
        this.idomor = idomor;
    }

    public void setIdomeds(ArrayList<IrBasicBlock> idomeds) {
        this.idomeds = idomeds;
    }

    public void setDF(ArrayList<IrBasicBlock> DF) {
        this.DF = DF;
    }

    public ArrayList<IrBasicBlock> getNextBlocks() {
        return nextBlocks;
    }

    public ArrayList<IrBasicBlock> getDomList() {
        return domList;
    }

    public IrBasicBlock getIdomor() {
        return idomor;
    }

    public ArrayList<IrBasicBlock> getIdomeds() {
        return idomeds;
    }

    public ArrayList<IrBasicBlock> getDF() {
        return DF;
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
}
