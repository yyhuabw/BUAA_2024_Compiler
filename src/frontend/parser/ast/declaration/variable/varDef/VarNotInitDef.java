package frontend.parser.ast.declaration.variable.varDef;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.ConstExp;
import frontend.parser.ast.terminal.Ident;
import middle.symbol.SymbolManager;
import middle.symbol.VarSymbol;
import middle.symbol.value.ValueType;

import java.util.ArrayList;

public class VarNotInitDef implements VarDefEle {
    private final Ident ident;
    private ArrayList<Token> leftBrackets = null;
    private ArrayList<ConstExp> constExps = null;
    private ArrayList<Token> rightBrackets = null;

    public VarNotInitDef(Ident ident) {
        this.ident = ident;
    }

    public VarNotInitDef(Ident ident, ArrayList<Token> leftBrackets, ArrayList<ConstExp> constExps, ArrayList<Token> rightBrackets) {
        this(ident);
        this.leftBrackets = leftBrackets;
        this.constExps = constExps;
        this.rightBrackets = rightBrackets;
    }

    public boolean addToSTAndCheck(ValueType valueType) {
        String name = ident.getToken().getContent();
        int dimension = leftBrackets.size();
        VarSymbol varSymbol = new VarSymbol(name, valueType, dimension);
        return SymbolManager.getInstance().addAndCheck(varSymbol);
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident.syntaxInfoOutput());
        for (int i = 0; i < leftBrackets.size(); i++) {
            sb.append(leftBrackets.get(i).syntaxInfoOutput());
            sb.append(constExps.get(i).syntaxInfoOutput());
            sb.append(rightBrackets.get(i).syntaxInfoOutput());
        }
        return sb.toString();
    }
}
