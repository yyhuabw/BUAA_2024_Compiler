package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrAluInstr;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.instruction.type_change.IrZextInstr;
import middle.llvm_ir.type.IrIntType;
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

    public int evaluate() {
        int ans = first.evaluate();

        for (int i = 0; i < operators.size(); i++) {
            switch (operators.get(i).getType()) {
                case PLUS:
                    ans += operands.get(i).evaluate();
                    break;
                case MINU:
                    ans -= operands.get(i).evaluate();
                    break;

                default:
                    System.out.println("Illegal operator in AddExp");
                    return 0;
            }
        }

        return ans;
    }

    // '+' | '−'
    @Override
    public IrValue genIR() {
        IrValue operand1 = first.genIR();
        if (!operand1.getType().isINT32() && !operands.isEmpty()) { // will calculate
            operand1 = new IrZextInstr(IrIntType.INT32, IrBuilder.getInstance().getLocalVarName(), operand1);
        }

        IrValue operand2;
        IrInstruction instruction;

        for (int i = 0; i < operands.size(); i++) {
            operand2 = operands.get(i).genIR();
            if (!operand2.getType().isINT32()) {
                operand2 = new IrZextInstr(IrIntType.INT32, IrBuilder.getInstance().getLocalVarName(), operand2);
            }

            switch (operators.get(i).getType()) {
                case PLUS:
                    instruction = new IrAluInstr(IrBuilder.getInstance().getLocalVarName(), IrAluInstr.Op.add, operand1, operand2);
                    break;
                case MINU:
                    instruction = new IrAluInstr(IrBuilder.getInstance().getLocalVarName(), IrAluInstr.Op.sub, operand1, operand2);
                    break;

                default:
                    System.out.println("Illegal operator in AddExp");
                    return null;
            }
            operand1 = instruction;
        }

        return operand1;
    }
}
