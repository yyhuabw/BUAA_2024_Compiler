package middle.symbol;

import middle.symbol.value.ValueType;

public class ConstSymbol extends Symbol {
    private final ValueType valueType;
    private final int dimension;

    public ConstSymbol(String name, ValueType valueType, int dimension) {
        super(name);
        this.valueType = valueType;
        this.dimension = dimension;

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

    public ValueType getValueType() {
        return valueType;
    }

    public int getDim() {
        return dimension;
    }
}
