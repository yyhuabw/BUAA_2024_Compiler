package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;

import java.util.ArrayList;

public class AddExp extends OpExp<MulExp> {
    // addition and subtraction expression
    public AddExp(MulExp first, ArrayList<Token> operators, ArrayList<MulExp> operands) {
        super(SyntaxType.ADD_EXP, first, operators, operands);
    }
}
