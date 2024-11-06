package frontend.parser.ast.expression.opExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;
import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrBuilder;

import java.util.ArrayList;

public class LOrExp extends OpExp<LAndExp> {
    // logic or expression
    public LOrExp(LAndExp first, ArrayList<Token> operators, ArrayList<LAndExp> operands) {
        super(SyntaxType.LOR_EXP, first, operators, operands);
    }

    /**
     * genIR() not use
     * operator must be ||
     */
    public void genIRForLOrExp(IrBasicBlock ifTrueBlock, IrBasicBlock ifFalseBlock) {
        ArrayList<LAndExp> lAndExps = new ArrayList<>();
        lAndExps.add(first);
        lAndExps.addAll(operands);

        for (int i = 0; i < lAndExps.size(); i++) {
            LAndExp lAndExp = lAndExps.get(i);

            if (i == lAndExps.size() - 1) { // the last LAndExp
                lAndExp.genIRForLAndExp(ifTrueBlock, ifFalseBlock);
            } else {
                IrBasicBlock nextBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());
                lAndExp.genIRForLAndExp(ifTrueBlock, nextBlock);
                IrBuilder.getInstance().setCurBlock(nextBlock);
            }
        }
    }
}
