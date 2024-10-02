package frontend.parser.ast.statement.block;

import frontend.parser.ast.SyntaxNode;

public class BlockItem implements SyntaxNode {
    private final BlockItemEle blockItemEle;

    public BlockItem(BlockItemEle blockItemEle) {
        this.blockItemEle = blockItemEle;
    }

    @Override
    public String syntaxInfoOutput() {
        return blockItemEle.syntaxInfoOutput();
    }
}
