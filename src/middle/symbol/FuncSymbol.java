package middle.symbol;

import middle.llvm_ir.function.IrFunction;
import middle.symbol.value.ValueType;

import java.util.ArrayList;

public class FuncSymbol extends Symbol {
    private final ValueType returnType;
    private ArrayList<VarSymbol> paramSymbols = new ArrayList<>(); // Func Formal Params
    private IrFunction irFunction;

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

    public void setParamSymbols(ArrayList<VarSymbol> paramSymbols) {
        this.paramSymbols = paramSymbols;
    }

    public void setIrFunction(IrFunction function) {
        this.irFunction = function;
    }

    public ValueType getReturnType() {
        return returnType;
    }

    public int getParamsSize() {
        return paramSymbols.size();
    }

    public ArrayList<VarSymbol> getParamSymbols() {
        return paramSymbols;
    }

    public IrFunction getIrFunction() {
        return irFunction;
    }
}
