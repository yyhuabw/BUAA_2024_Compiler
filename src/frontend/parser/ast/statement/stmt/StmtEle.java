package frontend.parser.ast.statement.stmt;

import frontend.parser.ast.SyntaxNode;
import middle.llvm_ir.IrValue;

public interface StmtEle extends SyntaxNode {
    @Override
    IrValue genIR();
}
