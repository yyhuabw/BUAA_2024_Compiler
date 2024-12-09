package processor.optimizer;

import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrModule;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.instruction.jump.IrRetInstr;
import middle.llvm_ir.instruction.jump.br.IrBrInstr;
import middle.llvm_ir.instruction.jump.br.IrCondBrInstr;
import middle.llvm_ir.instruction.jump.br.IrDirtBrInstr;

import java.util.HashSet;
import java.util.Iterator;

public class SimplifyBlock {
    private final IrModule module;

    public SimplifyBlock(IrModule module) {
        this.module = module;
    }

    public void run() {
        for (IrFunction function : module.getFuncList()) {
            for (IrBasicBlock block : function.getBlocks()) {
                deleteDupJumpInstr(block);
            }
        }

        for (IrFunction function : module.getFuncList()) {
            deleteDeadBlock(function);
        }
    }

    // delete redundant jump instruction
    private void deleteDupJumpInstr(IrBasicBlock block) {
        boolean canDelete = false;

        Iterator<IrInstruction> iterator = block.getInstrList().iterator();
        while (iterator.hasNext()) {
            IrInstruction instruction = iterator.next();
            if (canDelete) {
                iterator.remove();
                continue;
            }
            if (instruction instanceof IrBrInstr || instruction instanceof IrRetInstr) {
                canDelete = true;
            }
        }
    }

    // delete unreachable basic blocks
    private void deleteDeadBlock(IrFunction function) {
        IrBasicBlock entrance = function.getBlocks().get(0);
        HashSet<IrBasicBlock> visited = new HashSet<>();
        DFS(entrance, visited);

        Iterator<IrBasicBlock> iterator = function.getBlocks().iterator();
        while (iterator.hasNext()) {
            IrBasicBlock block = iterator.next();
            if (!visited.contains(block)) {
                iterator.remove();
                block.markDeleted();
            }
        }
    }

    // DFS for all reachable basic blocks
    private void DFS(IrBasicBlock entrance, HashSet<IrBasicBlock> visited) {
        visited.add(entrance);

        IrInstruction lastInstr = entrance.getLastInstr();
        if (lastInstr instanceof IrCondBrInstr condBrInstr) {
            IrBasicBlock ifTrueBlock = condBrInstr.getIfTrueBlock();
            IrBasicBlock ifFalseBlock = condBrInstr.getIfFalseBlock();
            if (!visited.contains(ifTrueBlock)) {
                DFS(ifTrueBlock, visited);
            }
            if (!visited.contains(ifFalseBlock)) {
                DFS(ifFalseBlock, visited);
            }
        } else if (lastInstr instanceof IrDirtBrInstr dirtBrInstr) {
            IrBasicBlock destBlock = dirtBrInstr.getDestBlock();
            if (!visited.contains(destBlock)) {
                DFS(destBlock, visited);
            }
        }
    }
}
