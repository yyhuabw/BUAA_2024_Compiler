package backend.mips.assembly.instruction;

import backend.mips.MipsBuilder;
import backend.mips.MipsNode;

public class MipsInstr implements MipsNode {
    public MipsInstr() {
        MipsBuilder.getInstance().addInstrToText(this);
    }

    @Override
    public String mipsOutput() {
        return null;
    }
}
