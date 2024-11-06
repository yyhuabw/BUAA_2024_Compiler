package middle.symbol;

import java.util.Stack;

public class SymbolManager {
    private static final SymbolManager SYMBOL_MANAGER = new SymbolManager();
    private final Stack<SymbolTable> symbolTableStack;
    private int loopDepth; // current loop layers
    private FuncSymbol curFunc; // current func copy, just for handling error, don't contain params
    private boolean isGlobal;

    private SymbolManager() {
        this.symbolTableStack = new Stack<>();
        this.loopDepth = 0;
        this.curFunc = null;
        this.isGlobal = false;
    }

    public static SymbolManager getInstance() {
        return SYMBOL_MANAGER;
    }

    public Symbol getSymbol(String name) {
        return symbolTableStack.peek().getSymbolInScopes(name);
    }

    public boolean addAndCheck(Symbol symbol) {
        SymbolTable topTable = symbolTableStack.peek();
        if (topTable.getSymbolThisScope(symbol.getName()) != null) { // add fail
            return true;
        }

        // add success
        topTable.addSymbol(symbol);
        return false;
    }

    public void enterScope() {
        SymbolTable symbolTable = new SymbolTable();
        if (!symbolTableStack.isEmpty()) {
            symbolTable.setParent(symbolTableStack.peek());
        }
        symbolTableStack.push(symbolTable);
    }

    public void leaveScope() {
        symbolTableStack.pop();
    }

    public FuncSymbol getCurFunc() {
        return curFunc;
    }

    public void enterFuncDef(FuncSymbol funcSymbol) {
        curFunc = funcSymbol;
        enterScope();
    }

    public void leaveFuncDef() {
        curFunc = null;
        leaveScope();
    }

    public boolean notInLoop() {
        return loopDepth <= 0;
    }

    public void enterLoop() {
        loopDepth++;
    }

    public void leaveLoop() {
        loopDepth--;
    }

    public void setGlobalStatus(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public boolean isGlobal() {
        return isGlobal;
    }
}
