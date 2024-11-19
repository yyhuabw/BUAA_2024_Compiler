package middle.llvm_ir.utils.constant;

import middle.llvm_ir.type.IrArrayType;
import middle.llvm_ir.type.IrType;

import java.util.ArrayList;

public class IrConstArray extends IrConstant {
    private final ArrayList<IrConstInt> values;
    private boolean needZeroInit = false;

    public IrConstArray(IrType type, ArrayList<IrConstInt> values) {
        super(type, "array const");
        this.values = values;
        if (this.values.isEmpty()) {
            this.needZeroInit = true;
        }
        fixValues();
    }

    /**
     * fill the remaining part with 0
     */
    private void fixValues() {
        int start = values.size();
        int eleNum = ((IrArrayType) getType()).getEleNum();
        IrType eleType = ((IrArrayType) getType()).getEleType();
        for (int i = start; i < eleNum; i++) {
            values.add(new IrConstInt(eleType, 0));
        }
    }

    public ArrayList<IrConstInt> getValues() {
        return values;
    }

    public boolean needZeroInit() {
        return needZeroInit;
    }

    public ArrayList<Integer> getAllValue() {
        ArrayList<Integer> allValue = new ArrayList<>();
        for (IrConstInt value : values) {
            allValue.add(value.getValue());
        }
        return allValue;
    }

    public int getIndexValue(int index) {
        return values.get(index).getValue();
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
        if (needZeroInit) {
            return getType().irOutput() + " zeroinitializer";
        } else {
            return getType().irOutput() + " [" + String.join(", ", getValuesInfo()) + "]";
        }
    }
}
