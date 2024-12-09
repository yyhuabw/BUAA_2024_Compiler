package processor.optimizer;

import backend.mips.Register;
import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrModule;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.instruction.type_change.IrZextInstr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RegAllocator {
    private final IrModule module;
    // mapping: variable -> register
    private HashMap<IrValue, Register> var2reg = null;
    // mapping: register -> variable
    private HashMap<Register, IrValue> reg2var = null;
    // registers for allocating
    private final ArrayList<Register> regs;

    public RegAllocator(IrModule module) {
        this.module = module;

        this.regs = new ArrayList<>();
        for (Register register : Register.values()) {
            // t0-t9, s0-s7
            if (register.ordinal() >= Register.T0.ordinal() && register.ordinal() <= Register.T9.ordinal()) {
                regs.add(register);
            }
        }
    }

    public void run() {
        for (IrFunction function : module.getFuncList()) {
            initAttr();

            allocaForBlock(function.getBlocks().getFirst());

            // write back
            function.setVar2reg(var2reg);
        }
    }

    private void initAttr() {
        var2reg = new HashMap<>();
        reg2var = new HashMap<>();
    }

    private void allocaForBlock(IrBasicBlock entrance) {
        // operand last use in block
        HashMap<IrValue, IrInstruction> lastUse = new HashMap<>();
        // the instruction in it can be used
        HashSet<IrValue> defSet = new HashSet<>();
        // won't be used in this block-dom-tree
        HashSet<IrValue> wontUsed = new HashSet<>();

        // get last use of variables
        for (IrInstruction instruction : entrance.getInstrList()) {
            for (IrValue operand : instruction.getOperands()) {
                lastUse.put(operand, instruction);
            }
        }

        for (IrInstruction instruction : entrance.getInstrList()) {
            // for those last-use and won't out
            // release the register and save it to wontUsed for recover
            // TODO: special judgment of phiInstr: because phi is parallel, we can't release
            for (IrValue operand : instruction.getOperands()) {
                if (lastUse.get(operand) == instruction &&
                        !entrance.getOut().contains(operand) &&
                        var2reg.containsKey(operand)) {
                    reg2var.remove(var2reg.get(operand));
                    wontUsed.add(operand);
                }
            }

            // instr is def and non zext|truc
            // zext -> simply direct mapping
            if (instruction.canBeUsed() && !(instruction instanceof IrZextInstr)) {
                defSet.add(instruction);

                Register reg = allocaForValue();
                // handle conflict
                if (reg2var.containsKey(reg)) {
                    var2reg.remove(reg2var.get(reg));
                }
                // update
                var2reg.put(instruction, reg);
                reg2var.put(reg, instruction);
            }
        }

        // traverse idom-tree
        for (IrBasicBlock idomed : entrance.getIdomeds()) {
            /*
             * ensure preorder traversal
             * if current register's variable is not in idomed's inSet
             * record the reg->var in buffer and release the register
             * buffer is for recover the reg->var
             */
            HashMap<Register, IrValue> buffer = new HashMap<>();
            // record
            for (Register reg : reg2var.keySet()) {
                IrValue var = reg2var.get(reg);
                if (!idomed.getIn().contains(var)) {
                    buffer.put(reg, var);
                }
            }
            // release
            for (Register reg : buffer.keySet()) {
                reg2var.remove(reg);
            }

            allocaForBlock(idomed);

            // recover
            for (Register reg : buffer.keySet()) {
                reg2var.put(reg, buffer.get(reg));
            }
        }

        // release current block's register
        for (IrValue value : defSet) {
            if (var2reg.containsKey(value)) {
                reg2var.remove(var2reg.get(value));
            }
        }

        // recover wontUsed
        // because brother-block may use the reg->var
        for (IrValue value : wontUsed) {
            if (var2reg.containsKey(value) && !defSet.contains(value)) {
                reg2var.put(var2reg.get(value), value);
            }
        }
    }

    private Register allocaForValue() {
        Set<Register> allocatedRegs = reg2var.keySet();
        for (Register register : regs) {
            if (!allocatedRegs.contains(register)) {
                return register;
            }
        }
        return regs.get(0); // reg.t0
    }
}
