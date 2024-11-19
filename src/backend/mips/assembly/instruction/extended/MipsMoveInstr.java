package backend.mips.assembly.instruction.extended;

import backend.mips.Register;
import backend.mips.assembly.instruction.MipsInstr;

// set dst to contents of src
public class MipsMoveInstr extends MipsInstr {
    private final Register dst;
    private final Register src;

    public MipsMoveInstr(Register dst, Register src) {
        super();
        this.dst = dst;
        this.src = src;
    }

    @Override
    public String mipsOutput() {
        return "move " + dst.mipsOutput() + ", " + src.mipsOutput() + "\n";
    }
}
