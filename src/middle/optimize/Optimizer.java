package middle.optimize;

import middle.llvm_ir.IrModule;

public class Optimizer {
    private static final Optimizer optimizer = new Optimizer();

    public static Optimizer getInstance() {
        return optimizer;
    }

    public void run(IrModule module) {
        new SimplifyBlock(module).run();
        new CFGBuilder(module).run();
        new Mem2Reg(module).run();

        new CheckTypeChangeInstr(module).run();
    }
}
