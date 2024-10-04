package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;

import java.util.ArrayList;

public class RelExp extends OpExp<AddExp> {
    // relational expression
    public RelExp(AddExp first, ArrayList<Token> operators, ArrayList<AddExp> operands) {
        super(SyntaxType.REL_EXP, first, operators, operands);
    }
}
