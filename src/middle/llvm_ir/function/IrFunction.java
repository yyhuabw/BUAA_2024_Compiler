package middle.llvm_ir.function;

import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrUser;
import middle.llvm_ir.type.IrFuncType;
import middle.llvm_ir.type.IrType;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class IrFunction extends IrUser {
    private final IrType returnType;
    private final ArrayList<IrFParam> params;
    private final ArrayList<IrBasicBlock> blocks;

    public IrFunction(String name, IrType returnType) {
        super(IrFuncType.FUNC, name);
        this.returnType = returnType;
        this.params = new ArrayList<>();
        this.blocks = new ArrayList<>();

        IrBuilder.getInstance().addFunc(this);
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
