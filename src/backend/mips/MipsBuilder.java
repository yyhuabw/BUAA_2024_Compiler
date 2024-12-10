package backend.mips;

import backend.mips.assembly.globalDecl.MipsGlobalDecl;
import backend.mips.assembly.instruction.MipsInstr;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.function.IrFParam;
import middle.llvm_ir.function.IrFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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

    public IrFunction getCurFunc() {
        return curFunc;
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

    public Integer getOrSetOffsetOf(IrValue value) {
        Integer offset = getOffsetOf(value);
        if (offset == null) {
            downwardCurOffset(4);
            offset = getCurStackOffset();
            addValueMapping(value, offset);
        }
        return offset;
    }

    // for we have reg-allocator
    public boolean useReg() {
        return var2reg != null;
    }

    public void allocaRegToParam(IrFParam param, Register register) {
        if (var2reg == null) {
            return;
        }
        var2reg.put(param, register);
    }

    public Register getRegFor(IrValue value) {
        // close reg-allocator
        if (var2reg == null) {
            return null;
        }

        return var2reg.get(value);
    }

    public ArrayList<Register> getAllocatedRegs() {
        if (var2reg == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(new HashSet<>(var2reg.values()));
    }

    public MipsModule getModule() {
        return module;
    }
}
