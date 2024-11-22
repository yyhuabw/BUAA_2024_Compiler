package middle.llvm_ir.instruction.jump.call;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.alu.MipsRIAluInstr;
import backend.mips.assembly.instruction.extended.MipsLiInstr;
import backend.mips.assembly.instruction.extended.MipsMoveInstr;
import backend.mips.assembly.instruction.jump.MipsJumpInstr;
import backend.mips.assembly.instruction.memory.MipsLoadInstr;
import backend.mips.assembly.instruction.memory.MipsStoreInstr;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.function.IrFParam;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrInstrType;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.utils.constant.IrConstInt;

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

    /**
     * the stack allocation strategy is as follows:
     * +-------------------------+ <- sp
     * |         ......          |
     * +-------------------------+ <- curOffset
     * |   allocated registers   |
     * +-------------------------+ <- curOffset - regNum * 4
     * |          $ra            |
     * +-------------------------+ <- new sp
     * |    func-real-params     |
     * |        |  a_i  |        |
     * |        | a_i+1 |        |
     * +-------------------------+
     */
    @Override
    public void genAsm() {
        super.genAsm();

        ArrayList<Register> allocatedRegs = MipsBuilder.getInstance().getAllocatedRegs();
        ArrayList<IrValue> params = getParams();
        int curOffset = MipsBuilder.getInstance().getCurStackOffset();
        IrFunction function = getFunction();

        // store allocated-registers
        int regNum = 0;
        for (Register register : allocatedRegs) {
            regNum++;
            new MipsStoreInstr(MipsStoreInstr.Op.sw, register, Register.SP, curOffset - regNum * 4);
        }

        // store $ra
        new MipsStoreInstr(MipsStoreInstr.Op.sw, Register.RA, Register.SP, curOffset - regNum * 4 - 4);

        // store func_real_params
        int paramNum = 0;
        for (IrValue param : params) {
            paramNum++;

            // a1-a3 (a0 is for IO only)
            if (paramNum <= 3 && MipsBuilder.getInstance().useReg()) {
                Register paramReg = paramIntoAReg(param, Register.getRegWithIndex(Register.A0, paramNum), curOffset, allocatedRegs);

                if (!MipsBuilder.getInstance().useReg()) {
                    new MipsStoreInstr(MipsStoreInstr.Op.sw, paramReg, Register.SP, curOffset - regNum * 4 - 4 - paramNum * 4);
                }
            } else { // need store-to-stack
                Register tmpReg = Register.K0;

                if (param instanceof IrConstInt constInt) {
                    new MipsLiInstr(tmpReg, constInt.getValue());
                } else {
                    Register paramReg = MipsBuilder.getInstance().getRegFor(param);
                    if (paramReg != null) {
                        if (param instanceof IrFParam) {
                            new MipsLoadInstr(MipsLoadInstr.Op.lw, tmpReg, Register.SP, curOffset - (allocatedRegs.indexOf(paramReg) + 1) * 4);
                        } else {
                            tmpReg = paramReg;
                        }
                    } else {
                        new MipsLoadInstr(MipsLoadInstr.Op.lw, tmpReg, Register.SP, MipsBuilder.getInstance().getOffsetOf(param));
                    }
                }

                // reserve space for a1-a3
                new MipsStoreInstr(MipsStoreInstr.Op.sw, tmpReg, Register.SP, curOffset - regNum * 4 - 4 - paramNum * 4);
            }
        }

        // sp -> new sp
        new MipsRIAluInstr(MipsRIAluInstr.Op.addiu, Register.SP, Register.SP, curOffset - regNum * 4 - 4);
        // jal
        new MipsJumpInstr(MipsJumpInstr.Op.jal, function.getName().substring(1));

        // recover ra
        new MipsLoadInstr(MipsLoadInstr.Op.lw, Register.RA, Register.SP, 0);
        // recover sp
        new MipsRIAluInstr(MipsRIAluInstr.Op.addiu, Register.SP, Register.SP, - curOffset + regNum * 4 + 4);

        // recover allocated-registers
        regNum = 0;
        for (Register register : allocatedRegs) {
            regNum++;
            new MipsLoadInstr(MipsLoadInstr.Op.lw, register, Register.SP, curOffset - regNum * 4);
        }

        // get func-return-value
        // deliver to subclass to achieve
    }

    // load param_value to a1-a3 for func_call
    private Register paramIntoAReg(IrValue param, Register reg, int curOffset, ArrayList<Register> allocatedRegs) {
        if (param instanceof IrConstInt constInt) {
            new MipsLiInstr(reg, constInt.getValue());
            return reg;
        }

        Register paramReg = MipsBuilder.getInstance().getRegFor(param);
        if (paramReg != null) {
            // 1. param is current function's formal_param
            // we need get it from stack for it may have been modified
            if (param instanceof IrFParam) {
                new MipsLoadInstr(MipsLoadInstr.Op.lw, reg, Register.SP, curOffset - (allocatedRegs.indexOf(paramReg) + 1) * 4);
            }
            // 2. param is allocated a global reg
            else {
                new MipsMoveInstr(reg, paramReg);
            }
            return reg;
        }

        // load-from-stack
        new MipsLoadInstr(MipsLoadInstr.Op.lw, reg, Register.SP, MipsBuilder.getInstance().getOffsetOf(param));
        return reg;
    }
}
