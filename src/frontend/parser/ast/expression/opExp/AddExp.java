package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;
import middle.symbol.value.ValueType;

import java.util.ArrayList;

public class AddExp extends OpExp<MulExp> {
    // addition and subtraction expression
    public AddExp(MulExp first, ArrayList<Token> operators, ArrayList<MulExp> operands) {
        super(SyntaxType.ADD_EXP, first, operators, operands);
    }

    public ValueType getValueType() {
        if (first.getDim() < 0) { // has undefined ident
            return null;
        }
        for (MulExp mulExp : operands) { // has undefined ident
            if (mulExp.getDim() < 0) {
                return null;
            }
        }

        if (first.getDim() != 0) {
            return first.getValueType();
        }
        for (MulExp mulExp : operands) {
            if (mulExp.getDim() != 0) {
                return mulExp.getValueType();
            }
        }
        return first.getValueType();
    }

    public int getDim() {
        if (first.getDim() < 0) { // has undefined ident
            return -1;
        }
        for (MulExp mulExp : operands) { // has undefined ident
            if (mulExp.getDim() < 0) {
                return -1;
            }
        }

        if (first.getDim() != 0) {
            return first.getDim();
        }
        for (MulExp mulExp : operands) {
            if (mulExp.getDim() != 0) {
                return mulExp.getDim();
            }
        }
        return 0;
    }
}
