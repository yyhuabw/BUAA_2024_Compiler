package frontend.parser.ast;

import middle.llvm_ir.IrValue;

public interface SyntaxNode {
    String syntaxInfoOutput();

    IrValue genIR();
}
