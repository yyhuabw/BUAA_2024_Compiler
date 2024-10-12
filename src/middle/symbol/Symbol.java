package middle.symbol;

public class Symbol {
    private SymbolType type;
    private final String name;
    private int scopeId; // scope number

    public Symbol(String name) {
        this.name = name;
    }

    public void setType(SymbolType type) {
        this.type = type;
    }

    public void setScopeId(int scopeId) {
        this.scopeId = scopeId;
    }

    public String getName() {
        return name;
    }

    public String symbolInfoOutput() {
        return scopeId + " " + name + " " + type;
    }
}
