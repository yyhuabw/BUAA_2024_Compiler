package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.expression.unaryExp.UnaryExp;
import middle.symbol.value.ValueType;

import java.util.ArrayList;

public class MulExp extends OpExp<UnaryExp> {
    // multiplication division modular expression
    public MulExp(UnaryExp first, ArrayList<Token> operators, ArrayList<UnaryExp> operands) {
        super(SyntaxType.MUL_EXP, first, operators, operands);
    }

    public ValueType getValueType() {
        if (first.getDim() < 0) { // has undefined ident
            return null;
        }
        for (UnaryExp unaryExp : operands) { // has undefined ident
            if (unaryExp.getDim() < 0) {
                return null;
            }
        }

        if (first.getDim() != 0) {
            return first.getValueType();
        }
        for (UnaryExp unaryExp : operands) {
            if (unaryExp.getDim() != 0) {
                return unaryExp.getValueType();
            }
        }
        return first.getValueType();
    }

    public int getDim() {
        if (first.getDim() < 0) { // has undefined ident
            return -1;
        }
        for (UnaryExp unaryExp : operands) { // has undefined ident
            if (unaryExp.getDim() < 0) {
                return -1;
            }
        }

        if (first.getDim() != 0) {
            return first.getDim();
        }
        for (UnaryExp unaryExp : operands) {
            if (unaryExp.getDim() != 0) {
                return unaryExp.getDim();
            }
        }
        return 0;
    }
}
