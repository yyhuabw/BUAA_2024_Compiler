package frontend.parser.ast.declaration.variable.initVal;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Exp;

import java.util.ArrayList;

public class InitArrayVal implements InitValEle {
    private final Token leftBrace;
    private Exp first = null;
    private ArrayList<Token> commas = null;
    private ArrayList<Exp> exps = null;
    private final Token rightBrace;

    public InitArrayVal(Token leftBrace, Token rightBrace) {
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
    }

    public InitArrayVal(Token leftBrace, Exp first, ArrayList<Token> commas, ArrayList<Exp> exps, Token rightBrace) {
        this(leftBrace, rightBrace);
        this.first = first;
        this.commas = commas;
        this.exps = exps;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(leftBrace.syntaxInfoOutput());
        if (first != null) {
            sb.append(first.syntaxInfoOutput());
            for (int i = 0; i < commas.size(); i++) {
                sb.append(commas.get(i).syntaxInfoOutput());
                sb.append(exps.get(i).syntaxInfoOutput());
            }
        }
        sb.append(rightBrace.syntaxInfoOutput());
        return sb.toString();
    }
}
