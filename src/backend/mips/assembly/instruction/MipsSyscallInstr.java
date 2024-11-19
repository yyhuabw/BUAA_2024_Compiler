package backend.mips.assembly.instruction;

public class MipsSyscallInstr extends MipsInstr {
    public MipsSyscallInstr() {
        super();
    }

    @Override
    public String mipsOutput() {
        return "syscall\n";
    }
}
