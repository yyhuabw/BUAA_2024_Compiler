package backend.mips.assembly.instruction.extended;

import backend.mips.Register;
import backend.mips.assembly.instruction.MipsInstr;

public class MipsLiInstr extends MipsInstr {
    private final Register result;
    private final int imm;

    public MipsLiInstr(Register result, int imm) {
        super();
        this.result = result;
        this.imm = imm;
    }

    @Override
    public String mipsOutput() {
        return "li " + result.mipsOutput() + ", " + imm + "\n";
    }
}
