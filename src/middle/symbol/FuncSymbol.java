package middle.symbol;

import middle.symbol.value.ValueType;

import java.util.ArrayList;

public class FuncSymbol extends Symbol {
    private final ValueType returnType;
    private ArrayList<VarSymbol> symbols = new ArrayList<>(); // Func Formal Params

    public FuncSymbol(String name, ValueType returnType) {
        super(name);
        this.returnType = returnType;

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

    public void setSymbols(ArrayList<VarSymbol> symbols) {
        this.symbols = symbols;
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
