package frontend.parser.ast.declaration.constant;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.declaration.constant.constInitVal.ConstInitVal;
import frontend.parser.ast.expression.single.ConstExp;
import frontend.parser.ast.terminal.Ident;

import java.util.ArrayList;

public class ConstDef implements SyntaxNode {
    private final SyntaxType type;
    private final Ident ident;
    private ArrayList<Token> leftBrackets = null;
    private ArrayList<ConstExp> constExps = null;
    private ArrayList<Token> rightBrackets = null;

    private final Token assign;
    private final ConstInitVal constInitVal;

    public ConstDef(Ident ident, Token assign, ConstInitVal constInitVal) {
        this.type = SyntaxType.CONST_DEF;
        this.ident = ident;
        this.assign = assign;
        this.constInitVal = constInitVal;
    }

    public ConstDef(Ident ident, ArrayList<Token> leftBrackets, ArrayList<ConstExp> constExps, ArrayList<Token> rightBrackets, Token assign, ConstInitVal constInitVal) {
        this(ident, assign, constInitVal);
        this.leftBrackets = leftBrackets;
        this.constExps = constExps;
        this.rightBrackets = rightBrackets;
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
        sb.append(assign.syntaxInfoOutput());
        sb.append(constInitVal.syntaxInfoOutput());
        sb.append(type.getName()).append("\n");
        return sb.toString();
    }
}
