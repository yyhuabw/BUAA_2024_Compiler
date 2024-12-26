package middle.symbol;

import java.util.HashMap;

public class SymbolTable {
    private final HashMap<String, Symbol> symbolMap; // name -> symbol
    private SymbolTable parent = null;

    public SymbolTable() {
        this.symbolMap = new HashMap<>();
    }

    public boolean hasParent() {
        return parent != null;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public void addSymbol(Symbol symbol) {
        symbolMap.put(symbol.getName(), symbol);
    }

    public Symbol getSymbolThisScope(String name) {
        if (symbolMap.containsKey(name)) {
            return symbolMap.get(name);
        }
        return null;
    }

    public Symbol getSymbolInScopes(String name) {
        if (symbolMap.containsKey(name)) {
            return symbolMap.get(name);
        }
        if (hasParent()) {
            return parent.getSymbolInScopes(name);
        }
        return null;
    }
}
