package middle.optimize;

import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrModule;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.function.IrFunction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class ActiveVarAnalyzer {
    private final IrModule module;
    private HashMap<IrBasicBlock, HashSet<IrValue>> inMap = null;
    private HashMap<IrBasicBlock, HashSet<IrValue>> outMap = null;

    public ActiveVarAnalyzer(IrModule module) {
        this.module = module;
    }

    public void run() {
        for (IrFunction function : module.getFuncList()) {
            initAttr(function);

            // generate def use set
            for (IrBasicBlock block : function.getBlocks()) {
                block.genDefUse();
            }

            // generate in out set
            genInOut(function);
        }
    }

    private void initAttr(IrFunction function) {
        inMap = new HashMap<>();
        outMap = new HashMap<>();

        for (IrBasicBlock block : function.getBlocks()) {
            inMap.put(block, new HashSet<>());
            outMap.put(block, new HashSet<>());
        }
    }

    private void genInOut(IrFunction function) {
        // flag -> mark is change or not
        boolean flag = true;

        LinkedList<IrBasicBlock> blockList = function.getBlocks();

        while (flag) {
            flag = false;

            // traverse from back to front
            for (int i = blockList.size() - 1; i >= 0; i--) {
                IrBasicBlock block = blockList.get(i);
                HashSet<IrValue> out  = new HashSet<>();

                // out = U(nextBlock's in)
                for (IrBasicBlock nextBlock : block.getNextBlocks()) {
                    out.addAll(inMap.get(nextBlock));
                }
                outMap.put(block, out);


                HashSet<IrValue> oldIn = inMap.get(block);
                // in = (out - def) + use
                HashSet<IrValue> newIn = new HashSet<>(out);
                newIn.removeAll(block.getDef());
                newIn.addAll(block.getUse());

                if (!newIn.equals(oldIn)) {
                    inMap.put(block, newIn);
                    flag = true;
                }
            }
        }

        // write back to block
        for (IrBasicBlock block : blockList) {
            block.setIn(inMap.get(block));
            block.setOut(outMap.get(block));
        }
    }
}
