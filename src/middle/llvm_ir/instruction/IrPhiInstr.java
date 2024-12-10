package middle.llvm_ir.instruction;

import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrType;

import java.util.ArrayList;

// <result> = phi [fast-math-flags] <ty> [<val0>, <label0>], ...
public class IrPhiInstr extends IrInstruction {
    private final ArrayList<IrBasicBlock> optionalBlocks;

    public IrPhiInstr(IrType type, String name) {
        super(type, name, IrInstrType.PHI);
        optionalBlocks = new ArrayList<>();
    }

    public void addOption(IrBasicBlock prevBlock, IrValue value) {
        optionalBlocks.add(prevBlock);
        addOperand(value);
    }

    public ArrayList<IrBasicBlock> getOptionalBlocks() {
        return optionalBlocks;
    }

    @Override
    public boolean canBeUsed() {
        return true;
    }

    @Override
    public String irOutput() {
        StringBuilder sb = new StringBuilder();

        sb.append(getName()).append(" = phi ").append(getType().irOutput()).append(" ");

        for (int i = 0; i < optionalBlocks.size(); i++) {
            IrBasicBlock prevBlock = optionalBlocks.get(i);
            IrValue value = getOperand(i);
            sb.append("[ ").append(value.getName()).append(", %").append(prevBlock.getName()).append(" ], ");
        }
        sb.delete(sb.length() - 2, sb.length());

        sb.append("\n");

        return sb.toString();
    }
}
