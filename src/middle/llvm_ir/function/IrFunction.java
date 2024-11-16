package middle.llvm_ir.function;

import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrUser;
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
    private HashMap<IrBasicBlock, IrBasicBlock> idomorMap; // dominated -> immediate dominator
    private HashMap<IrBasicBlock, ArrayList<IrBasicBlock>> idomedMap; // block -> immediate dominateds

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
}
