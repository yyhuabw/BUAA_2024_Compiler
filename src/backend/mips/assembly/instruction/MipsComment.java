package backend.mips.assembly.instruction;

public class MipsComment extends MipsInstr {
    private final String comment;

    public MipsComment(String comment) {
        super();
        this.comment = comment;
    }

    @Override
    public String mipsOutput() {
        return "\n# " + comment + "\n";
    }
}
