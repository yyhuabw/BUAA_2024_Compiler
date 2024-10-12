package middle.symbol;

import java.util.ArrayList;
import java.util.Stack;

public class SymbolManager {
    private static final SymbolManager SYMBOL_MANAGER = new SymbolManager();
    private final Stack<SymbolTable> symbolTableStack;
    private final ArrayList<SymbolTable> symbolTables; // all symbols for output
    private int loopDepth; // current loop layers
    private FuncSymbol curFunc; // current func copy, just for handling error, don't contain params

    private SymbolManager() {
        this.symbolTableStack = new Stack<>();
        this.symbolTables = new ArrayList<>();
        this.loopDepth = 0;
        this.curFunc = null;
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
        symbol.setScopeId(symbolTables.indexOf(topTable) + 1);
        return false;
    }

    public void enterScope() {
        SymbolTable symbolTable = new SymbolTable();
        if (!symbolTableStack.isEmpty()) {
            symbolTable.setParent(symbolTableStack.peek());
        }
        symbolTableStack.push(symbolTable);
        symbolTables.add(symbolTable);
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

    public String symbolInfoOutput() {
        StringBuilder sb = new StringBuilder();
        for (SymbolTable symbolTable : symbolTables) {
            sb.append(symbolTable.symbolInfoOutput());
        }
        return sb.toString();
    }
}
