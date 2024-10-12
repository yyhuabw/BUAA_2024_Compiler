package middle.symbol;

import middle.symbol.value.ValueType;

import java.util.ArrayList;

public class FuncSymbol extends Symbol {
    private final ValueType returnType;
    private final ArrayList<VarSymbol> symbols; // Func Formal Params

    public FuncSymbol(String name, ValueType returnType, ArrayList<VarSymbol> symbols) {
        super(name);
        this.returnType = returnType;
        this.symbols = symbols;

        setSymbolType();
    }

    private void setSymbolType() {
        if (returnType.equals(ValueType.CHAR)) {
            setType(SymbolType.CharFunc);
        } else if (returnType.equals(ValueType.INT)) {
            setType(SymbolType.IntFunc);
        } else if (returnType.equals(ValueType.VOID)) {
            setType(SymbolType.VoidFunc);
        }
    }

    public ValueType getReturnType() {
        return returnType;
    }

    public int getParamsSize() {
        return symbols.size();
    }

    public ArrayList<VarSymbol> getSymbols() {
        return symbols;
    }
}
