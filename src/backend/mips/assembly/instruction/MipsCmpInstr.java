package backend.mips.assembly.instruction;

import backend.mips.Register;

public class MipsCmpInstr extends MipsInstr {
    public enum Op {
        seq, // GPR[rd] = (GPR[rs] == GPR[rt]) ? 1 : 0
        sge, // GPR[rd] = (GPR[rs] >= GPR[rt]) ? 1 : 0
        sgt, // GPR[rd] = (GPR[rs] > GPR[rt]) ? 1 : 0
        sle, // GPR[rd] = (GPR[rs] <= GPR[rt]) ? 1 : 0
        slt, // GPR[rd] = (GPR[rs] < GPR[rt]) ? 1 : 0
        sne; // GPR[rd] = (GPR[rs] != GPR[rt]) ? 1 : 0
    }

    private final Op op;
    private final Register result;
    private final Register lOperand;
    private final Register rOperand;

    public MipsCmpInstr(Op op, Register result, Register lOperand, Register rOperand) {
        super();
        this.op = op;
        this.result = result;
        this.lOperand = lOperand;
        this.rOperand = rOperand;
    }

    @Override
    public String mipsOutput() {
        return op + " " + result.mipsOutput() + ", " +
                lOperand.mipsOutput() + ", " + rOperand.mipsOutput() + "\n";
    }
}
