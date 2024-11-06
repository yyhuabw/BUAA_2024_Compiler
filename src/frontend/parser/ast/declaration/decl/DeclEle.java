package frontend.parser.ast.declaration.decl;

import frontend.parser.ast.SyntaxNode;
import middle.llvm_ir.IrValue;

public interface DeclEle extends SyntaxNode {
    @Override
    IrValue genIR();
}
