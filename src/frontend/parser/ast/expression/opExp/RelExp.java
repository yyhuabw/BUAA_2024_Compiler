package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrIcmpInstr;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.instruction.type_change.IrZextInstr;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.utils.constant.IrConstInt;

import java.util.ArrayList;

public class RelExp extends OpExp<AddExp> {
    // relational expression
    public RelExp(AddExp first, ArrayList<Token> operators, ArrayList<AddExp> operands) {
        super(SyntaxType.REL_EXP, first, operators, operands);
    }

    // '<' | '>' | '<=' | '>='
    @Override
    public IrValue genIR() {
        IrValue operand1 = first.genIR();
        if (!operand1.getType().isINT32() && !operands.isEmpty()) { // will calculate
            operand1 = changeToI32(operand1);
        }

        IrValue operand2;
        IrInstruction instruction;

        for (int i = 0; i < operands.size(); i++) {
            operand2 = operands.get(i).genIR(); // must be i32
            if (!operand2.getType().isINT32()) {
                operand2 = changeToI32(operand2);
            }
            switch (operators.get(i).getType()) {
                case GRE: // >
                    instruction = new IrIcmpInstr(IrBuilder.getInstance().getLocalVarName(), IrIcmpInstr.Op.sgt, operand1, operand2);
                    break;
                case LSS: // <
                    instruction = new IrIcmpInstr(IrBuilder.getInstance().getLocalVarName(), IrIcmpInstr.Op.slt, operand1, operand2);
                    break;
                case GEQ: // >=
                    instruction = new IrIcmpInstr(IrBuilder.getInstance().getLocalVarName(), IrIcmpInstr.Op.sge, operand1, operand2);
                    break;
                case LEQ:
                    instruction = new IrIcmpInstr(IrBuilder.getInstance().getLocalVarName(), IrIcmpInstr.Op.sle, operand1, operand2);
                    break;

                default:
                    System.out.println("Illegal operator in RelExp");
                    return null;
            }
            operand1 = instruction;
        }

        return operand1;
    }

    private IrValue changeToI32(IrValue value) {
        if (value instanceof IrConstInt constInt) {
            return new IrConstInt(IrIntType.INT32, constInt.getValue());
        } else {
            return new IrZextInstr(IrIntType.INT32, IrBuilder.getInstance().getLocalVarName(), value);
        }
    }
}
