package processor.optimizer;

import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrModule;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.instruction.type_change.IrTruncInstr;
import middle.llvm_ir.instruction.type_change.IrZextInstr;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.utils.constant.IrConstInt;

import java.util.Iterator;

public class CheckTypeChangeInstr {
    private final IrModule module;

    public CheckTypeChangeInstr(IrModule module) {
        this.module = module;
    }

    public void run() {
        for (IrFunction function : module.getFuncList()) {
            for (IrBasicBlock block : function.getBlocks()) {
                checkAndModifyTypeChangeInstr(block);
            }
        }
    }

    private void checkAndModifyTypeChangeInstr(IrBasicBlock block) {
        Iterator<IrInstruction> iterator = block.getInstrList().iterator();
        while (iterator.hasNext()) {
            IrInstruction instruction = iterator.next();
            if (instruction instanceof IrZextInstr zextInstr && zextInstr.getOriginValue() instanceof IrConstInt constInt) { // i8 -> i32
                zextInstr.allUserChangeToNewValue(new IrConstInt(IrIntType.INT32, constInt.getValue()));
                iterator.remove();
            } else if (instruction instanceof IrTruncInstr truncInstr && truncInstr.getOriginValue() instanceof IrConstInt constInt) { // i32 -> i8
                int value = constInt.getValue() & 0xff;
                truncInstr.allUserChangeToNewValue(new IrConstInt(IrIntType.INT8, value));
                iterator.remove();
            }
        }
    }
}
