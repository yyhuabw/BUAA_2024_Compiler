package frontend.parser.ast.function.single;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.declaration.type.BType;
import frontend.parser.ast.expression.single.ConstExp;
import frontend.parser.ast.terminal.Ident;

import java.util.ArrayList;

public class FuncFParam implements SyntaxNode {
    private final SyntaxType type;
    private final BType bType;
    private final Ident ident;
    private Token firstLeftBracket = null;
    private Token firstRightBracket = null;
    // multi-dimentional array
    private ArrayList<Token> leftBrackets = null;
    private ArrayList<ConstExp> constExps = null;
    private ArrayList<Token> rightBrackets = null;

    public FuncFParam(BType bType, Ident ident) {
        this.type = SyntaxType.FUNC_FORMAL_PARAM;
        this.bType = bType;
        this.ident = ident;
    }

    public FuncFParam(BType bType,
                      Ident ident,
                      Token firstLeftBracket,
                      Token firstRightBracket) {
        this(bType, ident);
        this.firstLeftBracket = firstLeftBracket;
        this.firstRightBracket = firstRightBracket;
    }

    public FuncFParam(BType bType,
                      Ident ident,
                      Token firstLeftBracket,
                      Token firstRightBracket,
                      ArrayList<Token> leftBrackets,
                      ArrayList<ConstExp> constExps,
                      ArrayList<Token> rightBrackets) {
        this(bType, ident, firstLeftBracket, firstRightBracket);
        this.leftBrackets = leftBrackets;
        this.constExps = constExps;
        this.rightBrackets = rightBrackets;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(bType.syntaxInfoOutput());
        sb.append(ident.syntaxInfoOutput());
        if (firstLeftBracket != null) {
            sb.append(firstLeftBracket.syntaxInfoOutput());
            sb.append(firstRightBracket.syntaxInfoOutput());
            for (int i = 0; i < leftBrackets.size(); i++) {
                sb.append(leftBrackets.get(i).syntaxInfoOutput());
                sb.append(constExps.get(i).syntaxInfoOutput());
                sb.append(rightBrackets.get(i).syntaxInfoOutput());
            }
        }
        sb.append(type.getName()).append("\n");
        return sb.toString();
    }
}
