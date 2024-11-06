package frontend.parser.ast.declaration.variable.initVal;

import frontend.parser.ast.SyntaxNode;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrType;

public interface InitValEle extends SyntaxNode {
    IrValue genVarIR(IrType type);
}
