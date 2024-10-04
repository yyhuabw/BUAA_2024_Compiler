package frontend.parser.ast.expression.single;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;

import java.util.ArrayList;

public class FuncRParams implements SyntaxNode {
    private final SyntaxType type;
    private final Exp first;
    private final ArrayList<Token> commas;
    private final ArrayList<Exp> exps;

    public FuncRParams(Exp first, ArrayList<Token> commas, ArrayList<Exp> exps) {
        this.type = SyntaxType.FUNC_REAL_PARAMS;
        this.first = first;
        this.commas = commas;
        this.exps = exps;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(first.syntaxInfoOutput());
        for (int i = 0; i < commas.size(); i++) {
            sb.append(commas.get(i).syntaxInfoOutput());
            sb.append(exps.get(i).syntaxInfoOutput());
        }
        sb.append(type.getName()).append("\n");
        return sb.toString();
    }
}
