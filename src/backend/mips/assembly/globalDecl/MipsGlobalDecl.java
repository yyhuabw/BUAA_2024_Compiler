package backend.mips.assembly.globalDecl;

import backend.mips.MipsBuilder;
import backend.mips.MipsNode;

public class MipsGlobalDecl implements MipsNode {
    private final String name;

    public MipsGlobalDecl(String name) {
        this.name = name;

        MipsBuilder.getInstance().addDeclToData(this);
    }

    public String getName() {
        return name;
    }

    @Override
    public String mipsOutput() {
        return name + "\n";
    }
}
