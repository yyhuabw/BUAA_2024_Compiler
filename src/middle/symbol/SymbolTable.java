package middle.symbol;

import java.util.HashMap;

public class SymbolTable {
    private final HashMap<String, Symbol> dataSymbols; // name -> symbol
    private final HashMap<String, Symbol> funcSymbols; // name -> symbol
    private SymbolTable parent = null;

    public SymbolTable() {
        this.dataSymbols = new HashMap<>();
        this.funcSymbols = new HashMap<>();
    }

    public boolean hasParent() {
        return parent != null;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public void addSymbol(Symbol symbol) {
        if (symbol instanceof FuncSymbol) {
            funcSymbols.put(symbol.getName(), symbol);
        } else {
            dataSymbols.put(symbol.getName(), symbol);
        }
    }

    public Symbol getSymbolThisScope(String name, boolean isFuncSymbol) {
        if (isFuncSymbol) {
            if (funcSymbols.containsKey(name)) {
                return funcSymbols.get(name);
            }
        } else {
            if (dataSymbols.containsKey(name)) {
                return dataSymbols.get(name);
            }
        }
        return null;
    }

    public Symbol getSymbolInScopes(String name, boolean isFuncSymbol) {
        if (isFuncSymbol) {
            if (funcSymbols.containsKey(name)) {
                return funcSymbols.get(name);
            }
            if (hasParent()) {
                return parent.getSymbolInScopes(name, true);
            }
        } else {
            if (dataSymbols.containsKey(name)) {
                return dataSymbols.get(name);
            }
            if (hasParent()) {
                return parent.getSymbolInScopes(name, false);
            }
        }
        return null;
    }
}
