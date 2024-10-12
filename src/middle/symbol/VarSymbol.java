package middle.symbol;

import middle.symbol.value.ValueType;

public class VarSymbol extends Symbol {
    private final ValueType valueType;
    private final int dimension;

    public VarSymbol(String name, ValueType valueType, int dimension) {
        super(name);
        this.valueType = valueType;
        this.dimension = dimension;

        setSymbolType();
    }

    private void setSymbolType() {
        if (valueType.equals(ValueType.CHAR)) {
            if (dimension == 0) {
                setType(SymbolType.Char);
            } else {
                setType(SymbolType.CharArray);
            }
        } else if (valueType.equals(ValueType.INT)) {
            if (dimension == 0) {
                setType(SymbolType.Int);
            } else {
                setType(SymbolType.IntArray);
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
