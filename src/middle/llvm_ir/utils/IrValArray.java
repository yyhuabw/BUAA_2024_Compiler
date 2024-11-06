package middle.llvm_ir.utils;

import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrType;

import java.util.ArrayList;

public class IrValArray extends IrValue {
    private final ArrayList<IrValue> values;

    public IrValArray(IrType type, ArrayList<IrValue> values) {
        super(type, "var array");
        this.values = values;
    }

    public ArrayList<IrValue> getValues() {
        return values;
    }

    /**
     * not use
     * @return ""
     */
    @Override
    public String irOutput() {
        return "";
    }
}
