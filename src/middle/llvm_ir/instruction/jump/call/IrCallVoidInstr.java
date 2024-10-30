package middle.llvm_ir.instruction.jump.call;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.function.IrFunction;

import java.util.ArrayList;

/**
 * call void @funcName(...)
 */
public class IrCallVoidInstr extends IrCallInstr {
    public IrCallVoidInstr(String name, IrFunction function, ArrayList<IrValue> params) {
        super(name, function, params);
    }

    @Override
    public String irOutput() {
        return "call void " +
                getFunction().getName() + "(" +
                String.join(", ", getParamsInfo()) + ")\n";
    }
}
