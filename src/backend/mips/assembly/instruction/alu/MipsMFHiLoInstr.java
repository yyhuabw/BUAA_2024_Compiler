package backend.mips.assembly.instruction.alu;

import backend.mips.Register;
import backend.mips.assembly.instruction.MipsInstr;

public class MipsMFHiLoInstr extends MipsInstr {
    public enum Op {
        mfhi, // GPR[rd] = HI
        mflo; // GPR[rd] = LO
    }

    private final Op op;
    private final Register result;

    public MipsMFHiLoInstr(Op op, Register result) {
        super();
        this.op = op;
        this.result = result;
    }

    @Override
    public String mipsOutput() {
        return op + " " + result.mipsOutput() + "\n";
    }
}
