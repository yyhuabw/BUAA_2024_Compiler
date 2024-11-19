package middle.llvm_ir.instruction.jump.call;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;

import java.util.ArrayList;

/**
 * <result> =  call  [ret attrs]  <ty> <name>(<...args>)
 * call void @funcName(...)
 */
public class IrCallInstr extends IrInstruction {
    public IrCallInstr(String name, IrFunction function, ArrayList<IrValue> params) {
        super(function.getReturnType(), name, IrInstrType.CALL);
        addOperand(function);
        addOperands(params);
    }

    public IrFunction getFunction() {
        return (IrFunction) getOperand(0);
    }

    /**
     * the params are func_real_params
     * the element is IrValue
     */
    public ArrayList<IrValue> getParams() {
        return getOperands(1, getOperandsSize());
    }

    public ArrayList<String> getParamsInfo() {
        ArrayList<String> paramsInfo = new ArrayList<>();
        ArrayList<IrValue> params = getParams();
        for (IrValue param : params) {
            paramsInfo.add(param.getType().irOutput() + " " + param.getName());
        }
        return paramsInfo;
    }

    @Override
    public void genAsm() {
        super.genAsm();

        IrFunction function = getFunction();
        ArrayList<IrValue> params = getParams();
    }
}
