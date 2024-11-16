package middle.llvm_ir;

import middle.llvm_ir.function.IrFParam;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.utils.IrForLoop;
import middle.llvm_ir.utils.IrGlobalVar;
import middle.llvm_ir.utils.IrStrLiteral;

import java.util.HashMap;
import java.util.Stack;

public class IrBuilder {
    private static final IrBuilder IR_BUILDER = new IrBuilder();

    /**
     * AUTO_INSERT_MODE -> IrValue will be inserted automatically when "new"
     * DEFAULT_MODE     -> IrValue won't be inserted automatically
     */
    private static final int AUTO_INSERT_MODE = 1;
    private static final int DEFAULT_MODE = 0;
    private int mode = DEFAULT_MODE;

    /**
     * string literal    -> @.str.cnt
     * global variable   -> @name
     * function          -> @name
     * basicBlock label  -> b.cnt
     * local variable    -> %v.cnt
     * func formal param -> %p.cnt
     */
    private static final String STR_LITERAL_PREFIX = "@.str.";
    private static final String GLOBAL_VAR_PREFIX = "@";
    private static final String FUNC_PREFIX = "@";
    private static final String BLOCK_LABEL_PREFIX = "b.";
    private static final String LOCAL_VAR_PREFIX = "%v.";
    private static final String FUNC_PARAM_PREFIX = "%p.";

    private int strLiteralCnt;
    private final HashMap<IrFunction, Integer> blockCntMap;
    private final HashMap<IrFunction, Integer> varCntMap;
    private final HashMap<IrFunction, Integer> paramCntMap;

    private final IrModule module;
    private IrBasicBlock curBlock;
    private IrFunction curFunction;
    private final Stack<IrForLoop> loopStack;

    private IrBuilder() {
        this.strLiteralCnt = 0;
        this.blockCntMap = new HashMap<>();
        this.varCntMap = new HashMap<>();
        this.paramCntMap = new HashMap<>();

        this.module = new IrModule();
        this.curBlock = null;
        this.curFunction = null;
        this.loopStack = new Stack<>();
    }

    public static IrBuilder getInstance() {
        return IR_BUILDER;
    }

    public void setAutoInsertMode() {
        this.mode = AUTO_INSERT_MODE;
    }

    public void setDefaultMode() {
        this.mode = DEFAULT_MODE;
    }

    public boolean isAutoInsertMode() {
        return mode == AUTO_INSERT_MODE;
    }

    public IrModule getModule() {
        return module;
    }

    public void addStrLiteral(IrStrLiteral strLiteral) {
        module.addStrLiteral(strLiteral);
    }

    public void addGlobalVar(IrGlobalVar globalVar) {
        module.addGlobalVar(globalVar);
    }

    public void addFunc(IrFunction function) {
        module.addFunc(function);
    }

    public void setCurFunction(IrFunction function) {
        blockCntMap.put(function, 0);
        varCntMap.put(function, 0);
        paramCntMap.put(function, 0);
        curFunction = function;
    }

    public void curFuncAddBlock(IrBasicBlock block) {
        curFunction.addBlock(block);
        block.setParentFunc(curFunction);
    }

    public void curFuncAddParam(IrFParam param) {
        curFunction.addParam(param);
        param.setParentFunc(curFunction);
    }

    public void setCurBlock(IrBasicBlock block) {
        this.curBlock = block;
    }

    public void curBlockAddInstr(IrInstruction instr) {
        curBlock.addInstr(instr);
        instr.setParentBlock(curBlock);
    }

    public IrFunction getCurFunction() {
        return curFunction;
    }

    public void pushLoop(IrForLoop loop) {
        loopStack.push(loop);
    }

    public void popLoop() {
        loopStack.pop();
    }

    public IrForLoop getCurLoop() {
        return loopStack.peek();
    }

    /**
     * for llvm_ir naming
     * @return String
     */
    public String getStrLiteralName() {
        return STR_LITERAL_PREFIX + strLiteralCnt++;
    }

    /**
     * for llvm_ir naming
     * @return String
     */
    public String getGlobalVarName(String name) {
        return GLOBAL_VAR_PREFIX + name;
    }

    /**
     * for llvm_ir naming
     * @return String
     */
    public String getFuncName(String name) {
        return FUNC_PREFIX + name;
    }

    /**
     * for llvm_ir naming
     * @return String
     */
    public String getBlockLabelName() {
        int curIndex = blockCntMap.get(curFunction);
        blockCntMap.put(curFunction, curIndex + 1);
        return BLOCK_LABEL_PREFIX + curIndex;
    }

    /**
     * for llvm_ir naming
     * @return String
     */
    public String getLocalVarName() {
        int curIndex = varCntMap.get(curFunction);
        varCntMap.put(curFunction, curIndex + 1);
        return LOCAL_VAR_PREFIX + curIndex;
    }

    public String getLocalVarName(IrFunction function) { // for phiInstr
        int curIndex = varCntMap.get(function);
        varCntMap.put(function, curIndex + 1);
        return LOCAL_VAR_PREFIX + curIndex;
    }

    /**
     * for llvm_ir naming
     * @return String
     */
    public String getFuncParamName() {
        int curIndex = paramCntMap.get(curFunction);
        paramCntMap.put(curFunction, curIndex + 1);
        return FUNC_PARAM_PREFIX + curIndex;
    }
}
