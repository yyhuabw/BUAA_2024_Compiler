package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrAluInstr;
import middle.llvm_ir.instruction.IrInstruction;
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

    // '+' | '−'
    @Override
    public IrValue genIR() {
        IrValue operand1 = first.genIR();
        IrValue operand2;
        IrInstruction instruction;

        for (int i = 0; i < operands.size(); i++) {
            operand2 = operands.get(i).genIR();
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
