package backend.mips;

import backend.mips.assembly.globalDecl.MipsGlobalDecl;
import backend.mips.assembly.instruction.MipsInstr;
import backend.mips.assembly.instruction.MipsLabel;

import java.util.LinkedList;

public class MipsModule implements MipsNode {
    private final LinkedList<MipsGlobalDecl> dataSegment;
    private final LinkedList<MipsInstr> textSegment;

    public MipsModule() {
        this.dataSegment = new LinkedList<>();
        this.textSegment = new LinkedList<>();
    }

    public void addDeclToData(MipsGlobalDecl globalDecl) {
        dataSegment.add(globalDecl);
    }

    public void addInstrToText(MipsInstr instr) {
        textSegment.add(instr);
    }

    @Override
    public String mipsOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(".data\n");
        for (MipsGlobalDecl globalDecl : dataSegment) {
            sb.append("    ").append(globalDecl.mipsOutput());
        }
        sb.append("\n.text\n");
        for (MipsInstr instr : textSegment) {
            if (instr instanceof MipsLabel mipsLabel) {
                sb.append("\n").append(mipsLabel.mipsOutput());
            } else {
                sb.append("    ").append(instr.mipsOutput());
            }
        }
        return sb.toString();
    }
}
