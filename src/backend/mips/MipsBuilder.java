package backend.mips;

import backend.mips.assembly.globalDecl.MipsGlobalDecl;
import backend.mips.assembly.instruction.MipsInstr;

public class MipsBuilder {
    private static final MipsBuilder MIPS_BUILDER = new MipsBuilder();

    private final MipsModule module;

    private MipsBuilder() {
        this.module = new MipsModule();
    }

    public static MipsBuilder getInstance() {
        return MIPS_BUILDER;
    }

    public void addDeclToData(MipsGlobalDecl globalDecl) {
        module.addDeclToData(globalDecl);
    }

    public void addInstrToText(MipsInstr instr) {
        module.addInstrToText(instr);
    }

    public MipsModule getModule() {
        return module;
    }
}
