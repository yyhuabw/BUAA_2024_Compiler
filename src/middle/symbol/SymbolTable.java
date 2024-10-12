package middle.symbol;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    private final HashMap<String, Symbol> symbolMap; // name -> symbol
    private final ArrayList<Symbol> symbols;
    private SymbolTable parent = null;

    public SymbolTable() {
        this.symbolMap = new HashMap<>();
        this.symbols = new ArrayList<>();
    }

    public boolean hasParent() {
        return parent != null;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public void addSymbol(Symbol symbol) {
        symbolMap.put(symbol.getName(), symbol);
        symbols.add(symbol);
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

    public String symbolInfoOutput() {
        StringBuilder sb = new StringBuilder();
        for (Symbol symbol : symbols) {
            sb.append(symbol.symbolInfoOutput()).append("\n");
        }
        return sb.toString();
    }
}
