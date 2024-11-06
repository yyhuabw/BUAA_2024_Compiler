package frontend.parser.ast.declaration.decl;

import frontend.parser.ast.statement.block.BlockItemEle;
import middle.llvm_ir.IrValue;

public class Decl implements BlockItemEle {
    private final DeclEle declEle;

    public Decl(DeclEle declEle) {
        this.declEle = declEle;
    }

    @Override
    public String syntaxInfoOutput() {
        return declEle.syntaxInfoOutput();
    }

    @Override
    public IrValue genIR() {
        return declEle.genIR();
    }
}
