package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;

import java.util.ArrayList;

public class LOrExp extends OpExp<LAndExp> {
    // logic or expression
    public LOrExp(LAndExp first, ArrayList<Token> operators, ArrayList<LAndExp> operands) {
        super(SyntaxType.LOR_EXP, first, operators, operands);
    }
}
