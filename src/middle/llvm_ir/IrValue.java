package middle.llvm_ir;

import middle.llvm_ir.type.IrType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

public class IrValue implements IrNode {
    private final IrType type;
    private final String name;
    private final ArrayList<IrUse> useList; // the list of user <-> this

    public IrValue(IrType type, String name) {
        this.type = type;
        this.name = name;
        this.useList = new ArrayList<>();
    }

    public void addUse(IrUser user) {
        useList.add(new IrUse(user, this));
    }

    public void removeUse(IrUser user) {
        Iterator<IrUse> iterator = useList.iterator();
        while (iterator.hasNext()) {
            IrUse use = iterator.next();
            if (use.getUser().equals(user)) {
                iterator.remove();
                break;
            }
        }
    }

    public void allUserChangeToNewValue(IrValue newValue) {
        ArrayList<IrUser> userList = useList.stream().map(IrUse::getUser).collect(Collectors.toCollection(ArrayList::new));
        for (IrUser user : userList) {
            user.modifyOperand(this, newValue);
        }
    }

    public IrType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public ArrayList<IrUse> getUseList() {
        return useList;
    }

    @Override
    public String irOutput() {
        return "name: " + name + " type: " + type.irOutput() + "\n";
    }

    @Override
    public void genAsm() {
    }
}
