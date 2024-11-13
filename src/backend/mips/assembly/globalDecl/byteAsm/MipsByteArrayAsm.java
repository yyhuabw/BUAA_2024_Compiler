package backend.mips.assembly.globalDecl.byteAsm;

import java.util.ArrayList;

public class MipsByteArrayAsm extends MipsByteAsm {
    private final ArrayList<Integer> values;
    private final int size;

    public MipsByteArrayAsm(String name, ArrayList<Integer> values, int size) {
        super(name);
        this.values = values;
        this.size = size;
    }

    @Override
    public String mipsOutput() {
        if (values.isEmpty()) {
            return getName() + ": .byte 0:" + size + "\n";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(getName()).append(": .byte ").append(values.get(0));
            for (int i = 1; i < values.size(); i++) {
                sb.append(", ").append(values.get(i));
            }
            sb.append("\n");
            return sb.toString();
        }
    }
}
