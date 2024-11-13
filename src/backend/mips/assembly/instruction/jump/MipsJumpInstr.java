package backend.mips.assembly.instruction.jump;

import backend.mips.Register;
import backend.mips.assembly.instruction.MipsInstr;

public class MipsJumpInstr extends MipsInstr {
    public enum Op {
        j, // jump
        jal, // jump and link
        jr; // jump to GPR
    }

    private final Op op;
    private String target = null;
    private Register rs = null;

    public MipsJumpInstr(Op op, String target) {
        super();
        this.op = op;
        this.target = target;
    }

    public MipsJumpInstr(Op op, Register rs) {
        super();
        this.op = op;
        this.rs = rs;
    }

    @Override
    public String mipsOutput() {
        if (op == Op.jr) {
            return op + " " + rs.mipsOutput() + "\n";
        } else {
            return op + " " + target + "\n";
        }
    }
}
