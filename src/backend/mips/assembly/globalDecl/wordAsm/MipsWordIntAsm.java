package backend.mips.assembly.globalDecl.wordAsm;

public class MipsWordIntAsm extends MipsWordAsm {
    private final int value;

    public MipsWordIntAsm(String name, int value) {
        super(name);
        this.value = value;
    }

    @Override
    public String mipsOutput() {
        return getName() + ": .word " + value + "\n";
    }
}
