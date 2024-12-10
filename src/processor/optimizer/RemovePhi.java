package processor.optimizer;

import backend.mips.Register;
import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrModule;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.instruction.IrPhiInstr;
import middle.llvm_ir.instruction.jump.br.IrCondBrInstr;
import middle.llvm_ir.instruction.jump.br.IrDirtBrInstr;
import middle.llvm_ir.instruction.remove_phi.IrMoveInstr;
import middle.llvm_ir.instruction.remove_phi.IrPCInstr;

import java.util.*;

public class RemovePhi {
    private final IrModule module;

    public RemovePhi(IrModule module) {
        this.module = module;
    }

    public void run() {
        for (IrFunction function : module.getFuncList()) {
            // phi -> pc
            phi2PC(function);
            // pc -> move
            PC2Move(function);
        }
    }

    private void phi2PC(IrFunction function) {
        ArrayList<IrBasicBlock> blocks = new ArrayList<>(function.getBlocks());
        for (IrBasicBlock block : blocks) {
            // find the block that contains phi-instr
            if (!(block.getFirstInstr() instanceof IrPhiInstr)) {
                continue;
            }

            // add pc-instr
            ArrayList<IrBasicBlock> prevBlocks = block.getPrevBlocks();
            ArrayList<IrPCInstr> pcList = new ArrayList<>();
            prevBlocks.forEach((x) -> pcList.add(new IrPCInstr(IrBuilder.getInstance().getLocalVarName(function))));

            HashMap<IrBasicBlock, Integer> prevBlock2index = new HashMap<>();

            // insert pc-instr
            for (int i = 0; i < prevBlocks.size(); i++) {
                IrBasicBlock prevBlock = prevBlocks.get(i);
                IrPCInstr pcInstr = pcList.get(i);
                prevBlock2index.put(prevBlock, i);

                if (prevBlock.getNextBlocks().size() == 1) { // only one subsequent
                    insertPCToPrevBlock(pcInstr, prevBlock);
                } else {
                    insertPCToMidBlock(pcInstr, prevBlock, block);
                }
            }

            // phi -> pc
            Iterator<IrInstruction> iterator = block.getInstrList().iterator();
            while (iterator.hasNext()) {
                IrInstruction instruction = iterator.next();

                if (instruction instanceof IrPhiInstr phiInstr) {
                    ArrayList<IrValue> options = phiInstr.getOperands();
                    ArrayList<IrBasicBlock> optionalBlocks = phiInstr.getOptionalBlocks();
                    for (int i = 0; i < options.size(); i++) {
                        IrValue option = options.get(i);
                        IrBasicBlock optionalBlock = optionalBlocks.get(i);
                        pcList.get(prevBlock2index.get(optionalBlock)).addCopy(phiInstr, option);
                    }
                    iterator.remove();
                }
            }
        }
    }

    private void insertPCToPrevBlock(IrPCInstr pcInstr, IrBasicBlock prevBlock) {
        LinkedList<IrInstruction> instrList = prevBlock.getInstrList();
        IrInstruction lastInstr = instrList.getLast();
        // pc -> before lastInstr(br)
        instrList.add(instrList.indexOf(lastInstr), pcInstr);
        pcInstr.setParentBlock(prevBlock);
    }

    private void insertPCToMidBlock(IrPCInstr pcInstr, IrBasicBlock prevBlock, IrBasicBlock nextBlock) {
        // build mid-block
        IrFunction function = prevBlock.getParentFunc();
        IrBasicBlock midBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());
        midBlock.setParentFunc(function);
        function.getBlocks().add(function.getBlocks().indexOf(nextBlock), midBlock);

        // pc insert to mid-block
        midBlock.addInstr(pcInstr);
        pcInstr.setParentBlock(midBlock);

        // modify prev-block br-instr
        IrCondBrInstr condBrInstr = (IrCondBrInstr) prevBlock.getLastInstr();
        IrBasicBlock ifTrueBlock = condBrInstr.getIfTrueBlock();
        if (nextBlock.equals(ifTrueBlock)) {
            condBrInstr.setIfTrueBlock(midBlock);
        } else {
            condBrInstr.setIfFalseBlock(midBlock);
        }
        IrDirtBrInstr dirtBrInstr = new IrDirtBrInstr(nextBlock);
        midBlock.addInstr(dirtBrInstr);
        dirtBrInstr.setParentBlock(midBlock);

        // modify relation of prev-block and next-block
        prevBlock.getNextBlocks().add(prevBlock.getNextBlocks().indexOf(nextBlock), midBlock);
        prevBlock.getNextBlocks().remove(nextBlock);
        nextBlock.getPrevBlocks().add(nextBlock.getPrevBlocks().indexOf(prevBlock), midBlock);
        nextBlock.getPrevBlocks().remove(prevBlock);

        // add relation for mid-block
        midBlock.setPrevBlocks(new ArrayList<>());
        midBlock.getPrevBlocks().add(prevBlock);
        midBlock.setNextBlocks(new ArrayList<>());
        midBlock.getNextBlocks().add(nextBlock);
    }

    private void PC2Move(IrFunction function) {
        for (IrBasicBlock block : function.getBlocks()) {
            LinkedList<IrInstruction> instrList = block.getInstrList();
            if (instrList.size() >= 2 && instrList.get(instrList.size() - 2) instanceof IrPCInstr pcInstr) {
                instrList.remove(instrList.size() - 2);
                // pc -> move
                LinkedList<IrMoveInstr> moveList = convert(pcInstr);
                for (IrMoveInstr moveInstr : moveList) {
                    instrList.add(instrList.size() - 1, moveInstr);
                    moveInstr.setParentBlock(block);
                }
            }
        }
    }

    private LinkedList<IrMoveInstr> convert(IrPCInstr pcInstr) {
        ArrayList<IrValue> dstList = pcInstr.getDstList();
        ArrayList<IrValue> srcList = pcInstr.getSrcList();
        IrFunction function = pcInstr.getParentBlock().getParentFunc();
        HashMap<IrValue, Register> var2reg = function.getVar2reg();

        // initial move list
        LinkedList<IrMoveInstr> moveList = new LinkedList<>();
        for (int i = 0; i < dstList.size(); i++) {
            moveList.add(new IrMoveInstr(IrBuilder.getInstance().getLocalVarName(function),
                    dstList.get(i), srcList.get(i)));
        }

        ArrayList<IrMoveInstr> tmpList = new ArrayList<>();
        /*
         * handle situation of loop-assign
         *   move a, b
         *   move c, a
         * to
         *   move a0, a
         *   move a, b
         *   move c, a0
         */
        HashSet<IrValue> record = new HashSet<>();
        for (int i = 0; i < moveList.size(); i++) {
            IrValue dst = moveList.get(i).getDst();

            // handel loop-assign
            if (!record.contains(dst)) {
                // check
                boolean loopAssign = false;
                for (int j = i + 1; j < moveList.size(); j++) {
                    if (moveList.get(j).getSrc().equals(dst)) {
                        loopAssign = true;
                        break;
                    }
                }

                if (loopAssign) { // add temp-value
                    IrValue tmpValue = new IrValue(dst.getType(), "tmp_" + dst.getName());

                    // temp-value take place of dst
                    for (IrMoveInstr moveInstr : moveList) {
                        if (moveInstr.getSrc().equals(dst)) {
                            moveInstr.setSrc(tmpValue);
                        }
                    }

                    // insert temp-value at the start
                    tmpList.add(new IrMoveInstr(IrBuilder.getInstance().getLocalVarName(function),
                            tmpValue, dst));
                }

                record.add(dst);
            }
        }

        // handel reg-conflict
        record = new HashSet<>();
        for (int i = moveList.size() - 1; i >= 0; i--) {
            IrValue src = moveList.get(i).getSrc();

            if (!record.contains(src)) {
                // check
                boolean regConflict = false;
                for (int j = 0; j < i; j++) {
                    if (var2reg != null && var2reg.get(src) != null) {
                        Register srcReg = var2reg.get(src);
                        Register dstRegBefore = var2reg.get(moveList.get(j).getDst());

                        if (srcReg == dstRegBefore) {
                            regConflict = true;
                            break;
                        }
                    }
                }

                if (regConflict) { // add temp-value
                    IrValue tmpValue = new IrValue(src.getType(), "tmp_" + src.getName());

                    for (IrMoveInstr moveInstr : moveList) {
                        if (moveInstr.getSrc().equals(src)) {
                            moveInstr.setSrc(tmpValue);
                        }
                    }

                    tmpList.add(new IrMoveInstr(IrBuilder.getInstance().getLocalVarName(function),
                            tmpValue, src));
                }

                record.add(src);
            }
        }

        for (IrMoveInstr moveInstr : tmpList) {
            moveList.addFirst(moveInstr);
        }

        return moveList;
    }
}
