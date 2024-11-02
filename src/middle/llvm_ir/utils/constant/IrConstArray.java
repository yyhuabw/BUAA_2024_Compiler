package middle.llvm_ir.utils.constant;

import middle.llvm_ir.type.IrType;

import java.util.ArrayList;

public class IrConstArray extends IrConstant {
    private final ArrayList<IrConstInt> values;

    public IrConstArray(IrType type, ArrayList<IrConstInt> values) {
        super(type, "array const");
        this.values = values;
    }

    public ArrayList<String> getValuesInfo() {
        ArrayList<String> valuesInfo = new ArrayList<>();
        for (IrConstInt value : values) {
            valuesInfo.add(value.irOutput());
        }
        return valuesInfo;
    }

    @Override
    public String irOutput() {
        if (values == null) {
            return getType().irOutput() + " zeroinitializer";
        } else {
            return getType().irOutput() + " [" + String.join(", ", getValuesInfo()) + "]";
        }
    }
}
