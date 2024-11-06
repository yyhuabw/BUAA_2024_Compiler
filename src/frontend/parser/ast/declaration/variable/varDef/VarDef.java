package frontend.parser.ast.declaration.variable.varDef;

import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import middle.llvm_ir.IrValue;

public class VarDef implements SyntaxNode {
    private final SyntaxType type;
    private final VarDefEle varDefEle;

    public VarDef(VarDefEle varDefEle) {
        this.type = SyntaxType.VAR_DEF;
        this.varDefEle = varDefEle;
    }

    @Override
    public String syntaxInfoOutput() {
        return varDefEle.syntaxInfoOutput() + type.getName() + "\n";
    }

    @Override
    public IrValue genIR() {
        return varDefEle.genIR();
    }
}
