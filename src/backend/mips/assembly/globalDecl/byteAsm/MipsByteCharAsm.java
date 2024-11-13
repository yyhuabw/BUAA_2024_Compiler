package backend.mips.assembly.globalDecl.byteAsm;

public class MipsByteCharAsm extends MipsByteAsm {
    private final int value;

    public MipsByteCharAsm(String name, int value) {
        super(name);
        this.value = value;
    }

    @Override
    public String mipsOutput() {
        return getName() + ": .byte " + value + "\n";
    }
}
