package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;

import java.util.ArrayList;

public class EqExp extends OpExp<RelExp> {
    // equality expression
    public EqExp(RelExp first, ArrayList<Token> operators, ArrayList<RelExp> operands) {
        super(SyntaxType.EQ_EXP, first, operators, operands);
    }
}
