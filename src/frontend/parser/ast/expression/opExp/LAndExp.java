package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;

import java.util.ArrayList;

public class LAndExp extends OpExp<EqExp> {
    // logic and expression
    public LAndExp(EqExp first, ArrayList<Token> operators, ArrayList<EqExp> operands) {
        super(SyntaxType.LAND_EXP, first, operators, operands);
    }
}
