package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.expression.unaryExp.UnaryExp;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrAluInstr;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.instruction.type_change.IrZextInstr;
import middle.llvm_ir.type.IrIntType;
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

    public int evaluate() {
        int ans = first.evaluate();

        for (int i = 0; i < operators.size(); i++) {
            switch (operators.get(i).getType()) {
                case MULT:
                    ans *= operands.get(i).evaluate();
                    break;
                case DIV:
                    ans /= operands.get(i).evaluate();
                    break;
                case MOD:
                    ans %= operands.get(i).evaluate();
                    break;

                default:
                    System.out.println("Illegal operator in MulExp");
                    return 0;
            }
        }

        return ans;
    }

    // '*' | '/' | '%'
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
                case MULT:
                    instruction = new IrAluInstr(IrBuilder.getInstance().getLocalVarName(), IrAluInstr.Op.mul, operand1, operand2);
                    break;
                case DIV:
                    instruction = new IrAluInstr(IrBuilder.getInstance().getLocalVarName(), IrAluInstr.Op.sdiv, operand1, operand2);
                    break;
                case MOD:
                    instruction = new IrAluInstr(IrBuilder.getInstance().getLocalVarName(), IrAluInstr.Op.srem, operand1, operand2);
                    break;

                default:
                    System.out.println("Illegal operator in MulExp");
                    return null;
            }
            operand1 = instruction;
        }

        return operand1;
    }
}
