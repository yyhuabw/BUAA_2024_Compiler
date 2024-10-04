package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;

import java.util.ArrayList;

public class OpExp<T extends SyntaxNode> implements SyntaxNode {
    private final SyntaxType type;
    private final T first;
    private final ArrayList<Token> operators;
    private final ArrayList<T> operands;

    public OpExp(SyntaxType type, T first, ArrayList<Token> operators, ArrayList<T> operands) {
        this.type = type;
        this.first = first;
        this.operators = operators;
        this.operands = operands;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(first.syntaxInfoOutput());
        sb.append(type.getName()).append("\n");
        for (int i = 0; i < operators.size(); i++) {
            sb.append(operators.get(i).syntaxInfoOutput());
            sb.append(operands.get(i).syntaxInfoOutput());
            sb.append(type.getName()).append("\n");
        }
        return sb.toString();
    }
}
