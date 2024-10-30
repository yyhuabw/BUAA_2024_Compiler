package middle.llvm_ir;

/**
 * safeguard User <-> Value
 */
public class IrUse {
    private final IrUser user;
    private final IrValue value;

    public IrUse(IrUser user, IrValue value) {
        this.user = user;
        this.value = value;
    }

    public IrUser getUser() {
        return user;
    }

    public IrValue getValue() {
        return value;
    }
}
