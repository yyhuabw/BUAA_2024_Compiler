package backend.mips.assembly.instruction.alu;

import backend.mips.Register;
import backend.mips.assembly.instruction.MipsInstr;

public class MipsMulDivInstr extends MipsInstr {
    public enum Op {
        mult, // (HI, LO) = GPR[rs] × GPR[rt]
        div // LO = GPR[rs] / GPR[rt], HI = GPR[rs] % GPR[rt]
    }

    private final Op op;
    private final Register lOperand;
    private final Register rOperand;

    public MipsMulDivInstr(Op op, Register lOperand, Register rOperand) {
        super();
        this.op = op;
        this.lOperand = lOperand;
        this.rOperand = rOperand;
    }

    @Override
    public String mipsOutput() {
        return op + " " + lOperand.mipsOutput() + ", " + rOperand.mipsOutput() + "\n";
    }
}
