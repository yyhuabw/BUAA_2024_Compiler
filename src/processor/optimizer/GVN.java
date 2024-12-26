package processor.optimizer;

import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrModule;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrAluInstr;
import middle.llvm_ir.instruction.IrIcmpInstr;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.instruction.jump.call.IrCallInstr;
import middle.llvm_ir.instruction.memory.IrGEPInstr;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.utils.constant.IrConstInt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

// Global Variable Numbering
public class GVN {
    private final IrModule module;
    private HashMap<String, IrInstruction> GVNMap = null;

    public GVN(IrModule module) {
        this.module = module;
    }

    public void run() {
        for (IrFunction function : module.getFuncList()) {
            IrBasicBlock entrance = function.getBlocks().get(0);

            initAttr();

            GVNVisit(entrance);
        }
    }

    private void initAttr() {
        this.GVNMap = new HashMap<>();
    }

    private void GVNVisit(IrBasicBlock entrance) {
        // inserted instructions need deleted, avoiding affecting other dom-tree
        HashSet<IrInstruction> inserted = new HashSet<>();

        irAluInstrOptimize(entrance);

        Iterator<IrInstruction> iterator = entrance.getInstrList().iterator();
        while (iterator.hasNext()) {
            IrInstruction instruction = iterator.next();

            /*
             * the instruction that can be optimized are
             * alu, icmp, gep, call(func)
             */
            if (instruction instanceof IrAluInstr ||
                    instruction instanceof IrIcmpInstr ||
                    instruction instanceof IrGEPInstr ||
                    (instruction instanceof IrCallInstr callInstr && callInstr.canGVN())) {
                String hash = instruction.getGVNHash();

                // already have
                if (GVNMap.containsKey(hash)) {
                    instruction.allUserChangeToNewValue(GVNMap.get(hash));
                    iterator.remove();
                }
                // haven't yet, insert into map
                else {
                    GVNMap.put(hash, instruction);
                    inserted.add(instruction);
                }
            }
        }

        // traverse idom-tree
        for (IrBasicBlock idomed : entrance.getIdomeds()) {
            GVNVisit(idomed);
        }

        // avoid affecting other dom-tree
        for (IrInstruction instruction : inserted) {
            GVNMap.remove(instruction.getGVNHash());
        }
    }

    private void irAluInstrOptimize(IrBasicBlock block) {
        Iterator<IrInstruction> iterator = block.getInstrList().iterator();

        while (iterator.hasNext()) {
            IrInstruction instruction = iterator.next();

            if (!(instruction instanceof IrAluInstr aluInstr)) {
                continue; // only optimize alu-instr
            }

            // ALU Instruction
            IrAluInstr.Op op = aluInstr.getOp();
            IrValue operand1 = aluInstr.getOperand1();
            IrValue operand2 = aluInstr.getOperand2();

            int constNum = 0;
            if (operand1 instanceof IrConstInt) constNum++;
            if (operand2 instanceof IrConstInt) constNum++;

            if (constNum == 2) { // both are constant, calculate directly
                IrConstInt result = calcConstAndConst(op, (IrConstInt) operand1, (IrConstInt) operand2);
                aluInstr.allUserChangeToNewValue(result);
                iterator.remove();
            } else if (constNum == 1) { // only one const, need to check
                IrValue newValue = calcConstAndVar(op, operand1, operand2);
                if (newValue != null) { // mean that it can be optimized
                    aluInstr.allUserChangeToNewValue(newValue);
                    iterator.remove();
                }
            }
        }
    }

    private IrConstInt calcConstAndConst(IrAluInstr.Op op, IrConstInt constInt1, IrConstInt constInt2) {
        int result;
        int operand1 = constInt1.getValue();
        int operand2 = constInt2.getValue();

        // handle abnormal condition
        if ((op == IrAluInstr.Op.sdiv || op == IrAluInstr.Op.srem) && operand2 == 0) {
            return new IrConstInt(IrIntType.INT32, 0);
        }

        result = switch (op) {
            case add -> operand1 + operand2;
            case sub -> operand1 - operand2;
            case mul -> operand1 * operand2;
            case sdiv -> operand1 / operand2;
            case srem -> operand1 % operand2;
            case and -> operand1 & operand2;
            case or -> operand1 | operand2;
        };

        return new IrConstInt(IrIntType.INT32, result);
    }

    private IrValue calcConstAndVar(IrAluInstr.Op op, IrValue operand1, IrValue operand2) {
        switch (op) {
            case add: // check "a+0"
                if (operandIsConst(operand1, 0)) {
                    return operand2;
                }
                if (operandIsConst(operand2, 0)) {
                    return operand1;
                }
                break;
            case sub: // check "a-0"
                if (operandIsConst(operand2, 0)) {
                    return operand1;
                }
                break;
            case mul: // check "a*0", "a*1"
                // a*0
                if (operandIsConst(operand1, 0) || operandIsConst(operand2, 0)) {
                    return new IrConstInt(IrIntType.INT32, 0);
                }

                // a*1
                if (operandIsConst(operand1, 1)) {
                    return operand2;
                }
                if (operandIsConst(operand2, 1)) {
                    return operand1;
                }

                break;
            case sdiv: // check "a/1"
                if (operandIsConst(operand2, 1)) {
                    return operand1;
                }
                break;
            case srem: // check "a%1"
                if (operandIsConst(operand2, 1)) {
                    return new IrConstInt(IrIntType.INT32, 0);
                }
                break;
        }
        return null;
    }

    private boolean operandIsConst(IrValue operand, int value) {
        return operand instanceof IrConstInt constInt && constInt.getValue() == value;
    }
}
