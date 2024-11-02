package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;
import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.jump.br.IrCondBrInstr;

import java.util.ArrayList;

public class LAndExp extends OpExp<EqExp> {
    // logic and expression
    public LAndExp(EqExp first, ArrayList<Token> operators, ArrayList<EqExp> operands) {
        super(SyntaxType.LAND_EXP, first, operators, operands);
    }

    /**
     * genIR() not use
     * operator must be &&
     */
    public void genIRForLAndExp(IrBasicBlock ifTureBlock, IrBasicBlock ifFalseBlock) {
        ArrayList<EqExp> eqExps = new ArrayList<>();
        eqExps.add(first);
        eqExps.addAll(operands);

        for (int i = 0; i < eqExps.size(); i++) {
            EqExp eqExp = eqExps.get(i);
            IrValue cond = eqExp.genIR();
            if (i == eqExps.size() - 1) { // the last EqExp
                new IrCondBrInstr(IrBuilder.getInstance().getLocalVarName(), cond, ifTureBlock, ifFalseBlock);
            } else {
                IrBasicBlock nextBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());
                new IrCondBrInstr(IrBuilder.getInstance().getLocalVarName(), cond, nextBlock, ifFalseBlock);
                IrBuilder.getInstance().setCurBlock(nextBlock);
            }
        }
    }
}
