package backend.mips.assembly.instruction.alu;

import backend.mips.Register;
import backend.mips.assembly.instruction.MipsInstr;

public class MipsRIAluInstr extends MipsInstr {
    public enum Op {
        // calc_i
        addiu, // GPR[rt] = GPR[rs] + SignExt(Imm)
        andi, // GPR[rt] = GPR[rs] & ZeroExt(Imm)
        ori, // GPR[rt] = GPR[rs] | ZeroExt(Imm)
        xori, // GPR[rt] = GPR[rs] ^ ZeroExt(Imm)

        // shift
        sll, // GPR[rd] = GPR[rt] << s
        sra, // GPR[rd] = GPR[rt] >> s
        srl; // GPR[rd] = GPR[rt] >> s, logical
    }

    private final Op op;
    private final Register result;
    private final Register operand;
    private final int imm;

    public MipsRIAluInstr(Op op, Register result, Register operand, int imm) {
        super();
        this.op = op;
        this.result = result;
        this.operand = operand;
        this.imm = imm;
    }

    @Override
    public String mipsOutput() {
        return op + " " + result.mipsOutput() + ", " +
                operand.mipsOutput() + ", " + imm + "\n";
    }
}
