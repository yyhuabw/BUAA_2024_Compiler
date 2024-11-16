package middle.llvm_ir;

import middle.llvm_ir.type.IrType;

import java.util.ArrayList;
import java.util.List;

public class IrUser extends IrValue {
    private final ArrayList<IrValue> operands;

    public IrUser(IrType type, String name) {
        super(type, name);
        this.operands = new ArrayList<>();
    }

    public void addOperand(IrValue value) {
        operands.add(value);
        value.addUse(this);
    }

    public void addOperands(ArrayList<IrValue> values) {
        operands.addAll(values);
        for (IrValue value : values) {
            value.addUse(this);
        }
    }

    // ensure contain oldValue
    public void modifyOperand(IrValue oldValue, IrValue newValue) {
        int index = operands.indexOf(oldValue);
        oldValue.removeUse(this);
        operands.set(index, newValue);
        newValue.addUse(this);
    }

    public IrValue getOperand(int index) {
        return operands.get(index);
    }

    public ArrayList<IrValue> getOperands(int from, int to) {
        List<IrValue> subList =  operands.subList(from, to);
        return new ArrayList<>(subList);
    }

    public int getOperandsSize() {
        return operands.size();
    }
}
