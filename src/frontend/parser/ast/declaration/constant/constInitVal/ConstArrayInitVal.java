package frontend.parser.ast.declaration.constant.constInitVal;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.ConstExp;

import java.util.ArrayList;

public class ConstArrayInitVal implements ConstInitValEle {
    private final Token leftBrace;
    private ConstExp first = null;
    private ArrayList<Token> commas = null;
    private ArrayList<ConstExp> constExps = null;
    private final Token rightBrace;

    public ConstArrayInitVal(Token leftBrace, Token rightBrace) {
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
    }

    public ConstArrayInitVal(Token leftBrace, ConstExp first, Token rightBrace) {
        this(leftBrace, rightBrace);
        this.first = first;
    }

    public ConstArrayInitVal(Token leftBrace, ConstExp first, ArrayList<Token> commas, ArrayList<ConstExp> constExps, Token rightBrace) {
        this(leftBrace, first, rightBrace);
        this.commas = commas;
        this.constExps = constExps;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(leftBrace.syntaxInfoOutput());
        if (first != null) {
            sb.append(first.syntaxInfoOutput());
            for (int i = 0; i < commas.size(); i++) {
                sb.append(commas.get(i).syntaxInfoOutput());
                sb.append(constExps.get(i).syntaxInfoOutput());
            }
        }
        sb.append(rightBrace.syntaxInfoOutput());
        return sb.toString();
    }
}
