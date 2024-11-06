package frontend.parser.ast.declaration.variable.varDef;

import frontend.parser.ast.SyntaxNode;
import middle.llvm_ir.IrValue;

public interface VarDefEle extends SyntaxNode {
    @Override
    IrValue genIR();
}
