package backend.mips.assembly.instruction.memory;

import backend.mips.Register;
import backend.mips.assembly.instruction.MipsInstr;

public class MipsStoreInstr extends MipsInstr {
    public enum Op {
        sw, // memory[GPR[base]+offset] = GPR[rt]
        sh,
        sb;
    }

    private final Op op;
    private final Register rt;
    private final Register base;
    private final int offset;

    public MipsStoreInstr(Op op, Register rt, Register base, int offset) {
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
