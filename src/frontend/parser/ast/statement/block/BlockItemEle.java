package frontend.parser.ast.statement.block;

import frontend.parser.ast.SyntaxNode;
import middle.llvm_ir.IrValue;

public interface BlockItemEle extends SyntaxNode {
    @Override
    IrValue genIR();
}
