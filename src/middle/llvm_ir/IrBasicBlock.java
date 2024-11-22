package middle.llvm_ir;

import backend.mips.assembly.instruction.MipsLabel;
import middle.llvm_ir.function.IrFParam;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.instruction.IrPhiInstr;
import middle.llvm_ir.type.IrLabelType;
import middle.llvm_ir.utils.IrGlobalVar;

import java.util.ArrayList;
import java.util.HashSet;
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

    // active variable analysis
    private HashSet<IrValue> def;
    private HashSet<IrValue> use;
    private HashSet<IrValue> in;
    private HashSet<IrValue> out;

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

    // for active variable analysis
    public void genDefUse() {
        def = new HashSet<>();
        use = new HashSet<>();

        // phiInstr requires special handling because it is parallel
        for (IrInstruction instruction : instrList) {
            if (instruction instanceof IrPhiInstr phiInstr) {
                for (IrValue operand : phiInstr.getOperands()) {
                    if (operand instanceof IrInstruction || operand instanceof IrFParam || operand instanceof IrGlobalVar) {
                        use.add(operand);
                    }
                }
            }
        }

        for (IrInstruction instruction : instrList) {
            // use
            for (IrValue operand : instruction.getOperands()) {
                if (!def.contains(operand) && (operand instanceof IrInstruction || operand instanceof IrFParam || operand instanceof IrGlobalVar)) {
                    use.add(operand);
                }
            }

            /*
             * def
             * the instr should be the value that can be used
             */
            if (!use.contains(instruction) && instruction.canBeUsed()) {
                def.add(instruction);
            }
        }
    }

    public void setIn(HashSet<IrValue> in) {
        this.in = in;
    }

    public void setOut(HashSet<IrValue> out) {
        this.out = out;
    }

    public HashSet<IrValue> getDef() {
        return def;
    }

    public HashSet<IrValue> getUse() {
        return use;
    }

    public HashSet<IrValue> getIn() {
        return in;
    }

    public HashSet<IrValue> getOut() {
        return out;
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
