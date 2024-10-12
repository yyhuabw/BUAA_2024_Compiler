package middle.symbol;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;

public class SymbolManager {
    private static final SymbolManager SYMBOL_MANAGER = new SymbolManager();
    private final Stack<SymbolTable> symbolTableStack;
    private final ArrayList<Symbol> symbols; // all symbols for output
    private int scopeId; // current scope number
    private int loopDepth; // current loop layers
    private FuncSymbol curFunc; // current func copy, just for handling error, don't contain params

    private SymbolManager() {
        this.symbolTableStack = new Stack<>();
        this.symbols = new ArrayList<>();
        this.scopeId = 0;
        this.loopDepth = 0;
        this.curFunc = null;
    }

    public static SymbolManager getInstance() {
        return SYMBOL_MANAGER;
    }

    public Symbol getSymbol(String name, boolean isFuncSymbol) {
        return symbolTableStack.peek().getSymbolInScopes(name, isFuncSymbol);
    }

    public boolean addAndCheck(Symbol symbol) {
        SymbolTable topTable = symbolTableStack.peek();
        boolean isFuncSymbol = symbol instanceof FuncSymbol;
        if (topTable.getSymbolThisScope(symbol.getName(), isFuncSymbol) != null) { // add fail
            return true;
        }

        // add success
        if (isFuncSymbol) {
            symbolTableStack.get(0).addSymbol(symbol); // func def must in scope1
            symbol.setScopeId(1);
        } else {
            symbolTableStack.peek().addSymbol(symbol);
            symbol.setScopeId(scopeId);
        }
        symbols.add(symbol);
        return false;
    }

    public void enterScope() {
        SymbolTable symbolTable = new SymbolTable();
        if (!symbolTableStack.isEmpty()) {
            symbolTable.setParent(symbolTableStack.peek());
        }
        symbolTableStack.push(symbolTable);
        scopeId++;
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
        symbols.sort(Comparator.comparingInt(Symbol::getScopeId));

        StringBuilder sb = new StringBuilder();
        for (Symbol symbol : symbols) {
            sb.append(symbol.symbolInfoOutput()).append("\n");
        }

        return sb.toString();
    }
}
