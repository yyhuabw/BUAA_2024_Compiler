package backend.mips.assembly.instruction.memory;

import backend.mips.Register;
import backend.mips.assembly.instruction.MipsInstr;

public class MipsLoadInstr extends MipsInstr {
    public enum Op {
        lw, // GPR[rt] = memory[GPR[base]+offset]
        lh,
        lhu,
        lb,
        lbu
    }

    private final Op op;
    private final Register rt;
    private final Register base;
    private final int offset;

    public MipsLoadInstr(Op op, Register rt, Register base, int offset) {
        super();
        this.op = op;
        this.rt = rt;
        this.base = base;
        this.offset = offset;
    }

    @Override
    public String mipsOutput() {
        return op + " " + rt.mipsOutput() + ", " +
                offset + "(" + base.mipsOutput() + ")\n";
    }
}
