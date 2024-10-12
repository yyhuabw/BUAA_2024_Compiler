package frontend.parser.ast.terminal;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import middle.symbol.*;
import middle.symbol.value.ValueType;

public class Ident implements SyntaxNode {
    private final Token token;

    public Ident(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public int getLineno() {
        return token.getLineno();
    }

    public boolean queryIsConst() { // symbolTable has or not
        Symbol symbol = SymbolManager.getInstance().getSymbol(token.getContent());
        return symbol instanceof ConstSymbol;
    }

    public ValueType queryValueType() { // symbolTable has or not
        Symbol symbol = SymbolManager.getInstance().getSymbol(token.getContent());
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
        Symbol symbol = SymbolManager.getInstance().getSymbol(token.getContent());
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
}
