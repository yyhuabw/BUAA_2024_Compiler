package backend.mips.assembly.globalDecl;

public class MipsAsciizAsm extends MipsGlobalDecl {
    private final String content;

    public MipsAsciizAsm(String name, String content) {
        super(name);
        this.content = content;
    }

    @Override
    public String mipsOutput() {
        return getName() + ": .asciiz \"" + content + "\"\n";
    }
}
