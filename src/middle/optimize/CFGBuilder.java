package middle.optimize;

import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrModule;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.instruction.jump.br.IrCondBrInstr;
import middle.llvm_ir.instruction.jump.br.IrDirtBrInstr;

import java.util.*;

public class CFGBuilder {
    private final IrModule module;

    /*
     * the follow attributes are for each function
     * for each function, we will update them
     */
    // control flow graph
    private HashMap<IrBasicBlock, ArrayList<IrBasicBlock>> prevMap = null;
    private HashMap<IrBasicBlock, ArrayList<IrBasicBlock>> nextMap = null;
    // dominate set
    private HashMap<IrBasicBlock, ArrayList<IrBasicBlock>> domMap = null; // block -> blocks which are dominated by this
    // dominate graph|tree
    private HashMap<IrBasicBlock, IrBasicBlock> idomorMap; // dominated -> immediate dominator
    private HashMap<IrBasicBlock, ArrayList<IrBasicBlock>> idomedMap; // block -> immediate dominateds
    // dominance frontier
    private HashMap<IrBasicBlock, ArrayList<IrBasicBlock>> DFMap;

    public CFGBuilder(IrModule module) {
        this.module = module;
    }

    public void run() {
        for (IrFunction function : module.getFuncList()) {
            // Initialize auxiliary attributes
            initAttr(function);

            // generate control flow graph
            genCFG(function);

            // generate dom set
            genDomSet(function);

            // generate immediate dominate graph
            genIDG(function);

            // generate dominance frontier
            genDF(function);
        }
    }

    private void initAttr(IrFunction function) {
        prevMap = new HashMap<>();
        nextMap = new HashMap<>();
        domMap = new HashMap<>();
        idomorMap = new HashMap<>();
        idomedMap = new HashMap<>();
        DFMap = new HashMap<>();

        for (IrBasicBlock block : function.getBlocks()) {
            prevMap.put(block, new ArrayList<>());
            nextMap.put(block, new ArrayList<>());
            // domMap.put(block, new ArrayList<>());
            // idomorMap.put
            idomedMap.put(block, new ArrayList<>());
            DFMap.put(block, new ArrayList<>());
        }
    }

    // generate CFG
    private void genCFG(IrFunction function) {
        // get prev|next block
        for (IrBasicBlock block : function.getBlocks()) {
            IrInstruction lastInstr = block.getLastInstr();

            if (lastInstr instanceof IrCondBrInstr condBrInstr) {
                IrBasicBlock ifTrueBlock = condBrInstr.getIfTrueBlock();
                IrBasicBlock ifFalseBlock = condBrInstr.getIfFalseBlock();

                nextMap.get(block).add(ifTrueBlock);
                nextMap.get(block).add(ifFalseBlock);

                prevMap.get(ifTrueBlock).add(block);
                prevMap.get(ifFalseBlock).add(block);
            } else if (lastInstr instanceof IrDirtBrInstr dirtBrInstr) {
                IrBasicBlock destBlock = dirtBrInstr.getDestBlock();

                nextMap.get(block).add(destBlock);

                prevMap.get(destBlock).add(block);
            }
        }

        // write prev|next block info back to block
        for (IrBasicBlock block : function.getBlocks()) {
            block.setPrevBlocks(prevMap.get(block));
            block.setNextBlocks(nextMap.get(block));
        }

        // write prev|next block info back to function
        function.setPrevMap(prevMap);
        function.setNextMap(nextMap);
    }

    /**
     * IrBasicBlock -> ArrayList<IrBasicBlock>
     * gen the set of blocks which are dominated by the block
     */
    private void genDomSet(IrFunction function) {
        LinkedList<IrBasicBlock> blocks = function.getBlocks();
        IrBasicBlock entrance = blocks.getFirst(); // program entry block

        for (IrBasicBlock block : blocks) {
            HashSet<IrBasicBlock> reachedBlocks = new HashSet<>();
            DFSForNotDomBB(entrance, block, reachedBlocks);

            ArrayList<IrBasicBlock> domList = new ArrayList<>();
            for (IrBasicBlock candidateBlock : blocks) {
                if (!reachedBlocks.contains(candidateBlock)) {
                    domList.add(candidateBlock);
                }
            }

            domMap.put(block, domList);
            block.setDomList(domList);
        }
    }

    // DFS to find blocks which aren't dominated by the block
    private void DFSForNotDomBB(IrBasicBlock entrance, IrBasicBlock target, HashSet<IrBasicBlock> reachedBlocks) {
        if (entrance.equals(target)) { // have reached target
            return;
        }

        reachedBlocks.add(entrance); // target won't dom it

        for (IrBasicBlock nextBlock : entrance.getNextBlocks()) {
            if (!reachedBlocks.contains(nextBlock)) {
                DFSForNotDomBB(nextBlock, target, reachedBlocks);
            }
        }

        // won't reach target
    }

    // generate immediate dominate graph
    private void genIDG(IrFunction function) {
        LinkedList<IrBasicBlock> blocks = function.getBlocks();
        for (IrBasicBlock dominator : blocks) {
            for (IrBasicBlock dominated : dominator.getDomList()) {
                if (isIdom(dominator, dominated)) {
                    idomorMap.put(dominated, dominator);
                    idomedMap.get(dominator).add(dominated);
                }
            }
        }

        for (IrBasicBlock block : blocks) {
            block.setIdomor(idomorMap.get(block));
            block.setIdomeds(idomedMap.get(block));
        }

        function.setIdomorMap(idomorMap);
        function.setIdomedMap(idomedMap);
    }

    // judge is immediate-dominate relation or not
    private boolean isIdom(IrBasicBlock dominator, IrBasicBlock dominated) {
        if (notSdom(dominator, dominated)) {
            return false;
        }

        for (IrBasicBlock domedBlock : dominator.getDomList()) {
            if (!domedBlock.equals(dominated) && !domedBlock.equals(dominator) &&
                    domedBlock.getDomList().contains(dominated)) {
                return false;
            }
        }

        return true;
    }

    // judge is strict-dominate relation
    private boolean notSdom(IrBasicBlock dominator, IrBasicBlock dominated) {
        return !dominator.getDomList().contains(dominated) || dominator.equals(dominated);
    }

    /**
     * generate dominance frontier
     * following the logic of pseudocode
     */
    private void genDF(IrFunction function) {
        // traverse the CFG
        for (Map.Entry<IrBasicBlock, ArrayList<IrBasicBlock>> entry : nextMap.entrySet()) {
            IrBasicBlock a = entry.getKey();
            for (IrBasicBlock b : entry.getValue()) { // (a,b) is CFG's edge
                IrBasicBlock x = a;
                while (notSdom(x, b)) {
                    DFMap.get(x).add(b);
                    x = x.getIdomor();
                }
            }
        }

        for (IrBasicBlock block : function.getBlocks()) {
            block.setDF(DFMap.get(block));
        }
    }
}
