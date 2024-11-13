package backend.mips.assembly.instruction;

public class MipsLabel extends MipsInstr {
    private final String label;

    public MipsLabel(String label) {
        super();
        this.label = label;
    }

    @Override
    public String mipsOutput() {
        return label + ":\n";
    }
}
