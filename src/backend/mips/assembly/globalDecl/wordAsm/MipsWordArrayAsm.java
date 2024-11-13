package backend.mips.assembly.globalDecl.wordAsm;

import java.util.ArrayList;

public class MipsWordArrayAsm extends MipsWordAsm {
    private final ArrayList<Integer> values;
    private final int size;

    public MipsWordArrayAsm(String name, ArrayList<Integer> values, int size) {
        super(name);
        this.values = values;
        this.size = size;
    }

    @Override
    public String mipsOutput() {
        if (values.isEmpty()) {
            return getName() + ": .word 0:" + size + "\n";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(getName()).append(": .word ").append(values.get(0));
            for (int i = 1; i < values.size(); i++) {
                sb.append(", ").append(values.get(i));
            }
            sb.append("\n");
            return sb.toString();
        }
    }
}
