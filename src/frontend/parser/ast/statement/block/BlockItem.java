package frontend.parser.ast.statement.block;

import frontend.parser.ast.SyntaxNode;
import middle.llvm_ir.IrValue;

public class BlockItem implements SyntaxNode {
    private final BlockItemEle blockItemEle;

    public BlockItem(BlockItemEle blockItemEle) {
        this.blockItemEle = blockItemEle;
    }

    public BlockItemEle getBlockItemEle() {
        return blockItemEle;
    }

    @Override
    public String syntaxInfoOutput() {
        return blockItemEle.syntaxInfoOutput();
    }

    @Override
    public IrValue genIR() {
        return blockItemEle.genIR();
    }
}
