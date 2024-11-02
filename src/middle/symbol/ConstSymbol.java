package middle.symbol;

import middle.llvm_ir.IrValue;
import middle.symbol.value.ValueType;

public class ConstSymbol extends Symbol {
    private final ValueType valueType;
    private final int dimension;
    private IrValue irValue;

    public ConstSymbol(String name, ValueType valueType, int dimension) {
        super(name);
        this.valueType = valueType;
        this.dimension = dimension;
        this.irValue = null;

        setSymbolType();
    }

    private void setSymbolType() {
        if (valueType.equals(ValueType.CHAR)) {
            if (dimension == 0) {
                setType(SymbolType.ConstChar);
            } else {
                setType(SymbolType.ConstCharArray);
            }
        } else if (valueType.equals(ValueType.INT)) {
            if (dimension == 0) {
                setType(SymbolType.ConstInt);
            } else {
                setType(SymbolType.ConstIntArray);
            }
        }
    }

    public void setIrValue(IrValue value) {
        this.irValue = value;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public int getDim() {
        return dimension;
    }

    public IrValue getIrValue() {
        return irValue;
    }
}
