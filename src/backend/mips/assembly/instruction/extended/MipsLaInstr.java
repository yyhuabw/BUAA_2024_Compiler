package backend.mips.assembly.instruction.extended;

import backend.mips.Register;
import backend.mips.assembly.instruction.MipsInstr;

// set result to label's address
public class MipsLaInstr extends MipsInstr {
    private final Register result;
    private final String label;

    public MipsLaInstr(Register result, String label) {
        super();
        this.result = result;
        this.label = label;
    }

    @Override
    public String mipsOutput() {
        return "la " + result.mipsOutput() + ", " + label + "\n";
    }
}
