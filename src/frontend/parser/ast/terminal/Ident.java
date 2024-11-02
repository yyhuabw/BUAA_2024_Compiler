package frontend.parser.ast.terminal;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import middle.llvm_ir.IrValue;
import middle.symbol.*;
import middle.symbol.value.ValueType;

public class Ident implements SyntaxNode {
    private final Token token;
    private Symbol symbol = null;

    public Ident(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public int getLineno() {
        return token.getLineno();
    }

    public Symbol getSymbol() {
        if (this.symbol == null) {
            this.symbol = SymbolManager.getInstance().getSymbol(token.getContent());
        }
        return this.symbol;
    }

    public boolean queryIsConst() { // symbolTable has or not
        Symbol symbol = getSymbol();
        return symbol instanceof ConstSymbol;
    }

    public ValueType queryValueType() { // symbolTable has or not
        Symbol symbol = getSymbol();
        if (symbol instanceof VarSymbol varSymbol) {
            return varSymbol.getValueType();
        } else if (symbol instanceof ConstSymbol constSymbol) {
            return constSymbol.getValueType();
        } else if (symbol instanceof FuncSymbol funcSymbol) {
            return funcSymbol.getReturnType();
        }
        return null; // undefined ident
    }

    public int queryDim() { // symbolTable has or not
        Symbol symbol = getSymbol();
        if (symbol instanceof VarSymbol varSymbol) {
            return varSymbol.getDim();
        } else if (symbol instanceof ConstSymbol constSymbol) {
            return constSymbol.getDim();
        } else if (symbol instanceof FuncSymbol) {
            return 0;
        }
        return -1; // undefined ident
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput();
    }

    @Override
    public IrValue genIR() {
        Symbol symbol = getSymbol();
        if (symbol instanceof VarSymbol varSymbol) {
            return varSymbol.getIrValue();
        } else if (symbol instanceof ConstSymbol constSymbol) {
            return constSymbol.getIrValue();
        } else if (symbol instanceof FuncSymbol funcSymbol) {
            return funcSymbol.getIrFunction();
        }
        return null; // undefined ident
    }
}
