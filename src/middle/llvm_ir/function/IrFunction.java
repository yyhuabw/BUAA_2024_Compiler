package middle.llvm_ir.function;

import backend.mips.MipsBuilder;
import backend.mips.Register;
import backend.mips.assembly.instruction.MipsLabel;
import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrUser;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrFuncType;
import middle.llvm_ir.type.IrType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class IrFunction extends IrUser {
    private final IrType returnType;
    private final ArrayList<IrFParam> params;
    private final LinkedList<IrBasicBlock> blocks;

    // control flow graph
    private HashMap<IrBasicBlock, ArrayList<IrBasicBlock>> prevMap = null;
    private HashMap<IrBasicBlock, ArrayList<IrBasicBlock>> nextMap = null;
    // dominate graph|tree
    private HashMap<IrBasicBlock, IrBasicBlock> idomorMap = null; // dominated -> immediate dominator
    private HashMap<IrBasicBlock, ArrayList<IrBasicBlock>> idomedMap = null; // block -> immediate dominateds

    // register-Allocate
    private HashMap<IrValue, Register> var2reg = null;

    public IrFunction(String name, IrType returnType) {
        super(IrFuncType.FUNC, name);
        this.returnType = returnType;
        this.params = new ArrayList<>();
        this.blocks = new LinkedList<>();

        if (IrBuilder.getInstance().isAutoInsertMode()) {
            IrBuilder.getInstance().addFunc(this);
        }
    }

    public void addParam(IrFParam param) {
        params.add(param);
    }

    public void addBlock(IrBasicBlock block) {
        blocks.add(block);
    }

    public IrType getReturnType() {
        return returnType;
    }

    public boolean isVoid() {
        return returnType.isVoid();
    }

    public LinkedList<IrBasicBlock> getBlocks() {
        return blocks;
    }

    public void setPrevMap(HashMap<IrBasicBlock, ArrayList<IrBasicBlock>> prevMap) {
        this.prevMap = prevMap;
    }

    public void setNextMap(HashMap<IrBasicBlock, ArrayList<IrBasicBlock>> nextMap) {
        this.nextMap = nextMap;
    }

    public void setIdomorMap(HashMap<IrBasicBlock, IrBasicBlock> idomorMap) {
        this.idomorMap = idomorMap;
    }

    public void setIdomedMap(HashMap<IrBasicBlock, ArrayList<IrBasicBlock>> idomedMap) {
        this.idomedMap = idomedMap;
    }

    public void setVar2reg(HashMap<IrValue, Register> var2reg) {
        this.var2reg = var2reg;
    }

    public HashMap<IrValue, Register> getVar2reg() {
        return var2reg;
    }

    @Override
    public String irOutput() {
        String paramsInfo = params.stream().
                map(IrFParam::irOutput).
                collect(Collectors.joining(", "));

        return "define dso_local " +
                returnType.irOutput() + " " +
                getName() + "(" +
                paramsInfo + ") {\n" +

                blocks.stream().map(IrBasicBlock::irOutput).
                        collect(Collectors.joining("\n")) +

                "}\n\n";
    }

    @Override
    public void genAsm() {
        new MipsLabel(getName().substring(1));

        // enter a new function
        MipsBuilder.getInstance().enterFunc(this);

        // func_formal_param -> offset
        for (int i = 0; i < params.size(); i++) {
            IrFParam param = params.get(i);
            if (i < 3) { // a1-a3
                MipsBuilder.getInstance().allocaRegToParam(param,
                        Register.getRegWithIndex(Register.A0, i + 1));
            }
            MipsBuilder.getInstance().downwardCurOffset(4);
            // the first 3 mapping to empty-value-offset
            MipsBuilder.getInstance().addValueMapping(param, MipsBuilder.getInstance().getCurStackOffset());
        }

        for (IrBasicBlock block : blocks) {
            block.genAsm();
        }
    }
}
