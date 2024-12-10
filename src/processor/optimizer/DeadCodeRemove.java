package processor.optimizer;

import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrModule;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.instruction.jump.call.IrCallInstr;
import middle.llvm_ir.instruction.jump.io.IrIOInstr;

import java.util.Iterator;

public class DeadCodeRemove {
    private final IrModule module;

    public DeadCodeRemove(IrModule module) {
        this.module = module;
    }

    public void run() {
        for (IrFunction function : module.getFuncList()) {
            for (IrBasicBlock block : function.getBlocks()) {
                Iterator<IrInstruction> iterator = block.getInstrList().iterator();
                while (iterator.hasNext()) {
                    IrInstruction instruction = iterator.next();

                    /*
                     * the instruction that can be removed means that no one use it
                     * that's to say, the useList of it is empty
                     * so, we need to find the instruction that
                     * 1. can be used
                     * 2. can't be IOInstr -> it need be reserved for IO
                     * 3. can't be callInstr that can't be GVN -> side effects
                     * 4. the useList of it is empty
                     */
                    if (instruction.canBeUsed()) { // 1
                        if (!(instruction instanceof IrIOInstr)) { // 2
                            if (!(instruction instanceof IrCallInstr callInstr && !callInstr.canGVN())) { // 3
                                if (instruction.getUseList().isEmpty()) { // 4
                                    iterator.remove();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
