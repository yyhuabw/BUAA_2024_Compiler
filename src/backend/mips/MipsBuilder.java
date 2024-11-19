package backend.mips;

import backend.mips.assembly.globalDecl.MipsGlobalDecl;
import backend.mips.assembly.instruction.MipsInstr;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.function.IrFunction;

import java.util.HashMap;

/**
 * $sp is always at the bottom of the stack
 * value -> $sp - offset
 * avoid "addi $sp, $sp, -offset" at first
 * when jump to other function reset the $sp
 */
public class MipsBuilder {
    private static final MipsBuilder MIPS_BUILDER = new MipsBuilder();

    private final MipsModule module;
    private IrFunction curFunc = null;
    // $sp - offset
    private int curStackOffset = 0;
    // value->$sp-offset in stack
    private HashMap<IrValue, Integer> stackMap = null;
    // for register-allocate
    private HashMap<IrValue, Register> var2reg = null;

    private MipsBuilder() {
        this.module = new MipsModule();
    }

    public static MipsBuilder getInstance() {
        return MIPS_BUILDER;
    }

    /**
     * when enter the function
     * initialize the attributes
     */
    public void enterFunc(IrFunction function) {
        this.curFunc = function;
        this.curStackOffset = 0;
        this.stackMap = new HashMap<>();
        this.var2reg = function.getVar2reg();
    }

    public void addDeclToData(MipsGlobalDecl globalDecl) {
        module.addDeclToData(globalDecl);
    }

    public void addInstrToText(MipsInstr instr) {
        module.addInstrToText(instr);
    }

    public void downwardCurOffset(int offset) {
        curStackOffset -= offset;
    }

    public int getCurStackOffset() {
        return curStackOffset;
    }

    // should follow store-instruction
    public void addValueMapping(IrValue value, int offset) {
        stackMap.put(value, offset);
    }

    public Integer getOffsetOf(IrValue value) {
        return stackMap.get(value);
    }

    public Register getRegFor(IrValue value) {
        // close reg-allocator
        if (var2reg == null) {
            return null;
        }

        return var2reg.get(value);
    }

    public MipsModule getModule() {
        return module;
    }
}
