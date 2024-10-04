package frontend.parser.ast.expression.primaryExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.expression.single.Exp;
import frontend.parser.ast.terminal.Ident;

import java.util.ArrayList;

public class LVal implements PrimaryExpEle {
    private final SyntaxType type;
    private final Ident ident;
    private final ArrayList<Token> leftBrackets;
    private final ArrayList<Exp> exps;
    private final ArrayList<Token> rightBrackets;

    public LVal(Ident ident, ArrayList<Token> leftBrackets, ArrayList<Exp> exps, ArrayList<Token> rightBrackets) {
        this.type = SyntaxType.LVAL;
        this.ident = ident;
        this.leftBrackets = leftBrackets;
        this.exps = exps;
        this.rightBrackets = rightBrackets;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident.syntaxInfoOutput());
        for (int i = 0; i < leftBrackets.size(); i++) {
            sb.append(leftBrackets.get(i).syntaxInfoOutput());
            sb.append(exps.get(i).syntaxInfoOutput());
            sb.append(rightBrackets.get(i).syntaxInfoOutput());
        }
        sb.append(type.getName()).append("\n");
        return sb.toString();
    }
}
