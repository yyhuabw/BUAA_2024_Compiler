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

public class EqExp extends OpExp<RelExp> {
    // equality expression
    public EqExp(RelExp first, ArrayList<Token> operators, ArrayList<RelExp> operands) {
        super(SyntaxType.EQ_EXP, first, operators, operands);
    }

    // '==' | '!='
    @Override
    public IrValue genIR() {
        IrValue operand1 = first.genIR();
        IrValue operand2;
        IrInstruction instruction;

        if (operators.isEmpty()) { // only have RelExp
            if (!operand1.getType().isINT1()) { // ensure @return is i1
                operand1 = new IrIcmpInstr(IrBuilder.getInstance().getLocalVarName(), IrIcmpInstr.Op.ne,
                        operand1, new IrConstInt(IrIntType.INT32, 0));
            }
            return operand1;
        }

        for (int i = 0; i < operands.size(); i++) {
            if (!operand1.getType().isINT32()) { // change to i32
                operand1 = new IrZextInstr(IrIntType.INT32, IrBuilder.getInstance().getLocalVarName(), operand1);
            }

            operand2 = operands.get(i).genIR();
            if (!operand2.getType().isINT32()) { // change to i32
                operand2 = new IrZextInstr(IrIntType.INT32, IrBuilder.getInstance().getLocalVarName(), operand2);
            }

            switch (operators.get(i).getType()) {
                case EQL: // ==
                    instruction = new IrIcmpInstr(IrBuilder.getInstance().getLocalVarName(), IrIcmpInstr.Op.eq, operand1, operand2);
                    break;
                case NEQ: // !=
                    instruction = new IrIcmpInstr(IrBuilder.getInstance().getLocalVarName(), IrIcmpInstr.Op.ne, operand1, operand2);
                    break;

                default:
                    System.out.println("Illegal operator in EqExp");
                    return null;
            }
            operand1 = instruction;
        }

        return operand1;
    }
}
