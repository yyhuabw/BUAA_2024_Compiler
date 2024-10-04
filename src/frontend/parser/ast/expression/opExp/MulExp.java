package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.expression.unaryExp.UnaryExp;

import java.util.ArrayList;

public class MulExp extends OpExp<UnaryExp> {
    // multiplication division modular expression
    public MulExp(UnaryExp first, ArrayList<Token> operators, ArrayList<UnaryExp> operands) {
        super(SyntaxType.MUL_EXP, first, operators, operands);
    }
}
