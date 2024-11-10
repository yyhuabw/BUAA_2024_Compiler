package middle.llvm_ir;

import middle.llvm_ir.type.IrType;

import java.util.ArrayList;

public class IrValue implements IrNode {
    private final IrType type;
    private final String name;
    private final ArrayList<IrUse> uses; // the list of user <-> this

    public IrValue(IrType type, String name) {
        this.type = type;
        this.name = name;
        this.uses = new ArrayList<>();
    }

    public IrType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String irOutput() {
        return "name: " + name + " type: " + type.irOutput() + "\n";
    }
}
