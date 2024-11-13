package backend.mips.assembly.instruction.alu;

import backend.mips.Register;
import backend.mips.assembly.instruction.MipsInstr;

public class MipsRRAluInstr extends MipsInstr {
    public enum Op {
        // calc_R
        addu, // GPR[rd] = GPR[rs] + GPR[rt]
        subu, // GPR[rd] = GPR[rs] - GPR[rt]
        and, // GPR[rd] = GPR[rs] & GPR[rt]
        or, // GPR[rd] = GPR[rs] | GPR[rt]
        xor, // GPR[rd] = GPR[rs] ^ GPR[rt]
        nor, // GPR[rd] = ~(GPR[rs] | GPR[rt])

        // shift_v
        sllv, // GPR[rd] = GPR[rt] << GPR[rs]
        srav, // GPR[rd] = GPR[rt] >> GPR[rs]
        srlv // GPR[rd] = GPR[rt] >> GPR[rs], logical
    }

    private final Op op;
    private final Register result;
    private final Register lOperand;
    private final Register rOperand;

    public MipsRRAluInstr(Op op, Register result, Register lOperand, Register rOperand) {
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
