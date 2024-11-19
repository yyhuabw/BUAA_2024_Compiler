package backend.mips.assembly.instruction.jump;

import backend.mips.Register;
import backend.mips.assembly.instruction.MipsInstr;

public class MipsBranchInstr extends MipsInstr {
    public enum Op {
        beq, // (GPR[rs] == GPR[rt]) then transfer
        bne, // (GPR[rs] != GPR[rt]) then transfer

        blez, // (GPR[rs] <= 0) then transfer
        bgtz, // (GPR[rs] > 0) then transfer
        bltz, // (GPR[rs] < 0) then transfer
        bgez // (GPR[rs] >= 0) then transfer
    }

    private final Op op;
    private final Register rs;
    private Register rt = null;
    private final String label;

    public MipsBranchInstr(Op op, Register rs, String label) {
        super();
        this.op = op;
        this.rs = rs;
        this.label = label;
    }

    public MipsBranchInstr(Op op, Register rs, Register rt, String label) {
        this(op, rs, label);
        this.rt = rt;
    }

    @Override
    public String mipsOutput() {
        if (op == Op.beq || op == Op.bne) {
            return op + " " + rs.mipsOutput() + ", " +
                    rt.mipsOutput() + ", " + label + "\n";
        } else {
            return op + " " + rs.mipsOutput() + ", " +
                    label + "\n";
        }
    }
}
