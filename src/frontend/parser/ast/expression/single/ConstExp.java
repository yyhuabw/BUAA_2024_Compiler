package frontend.parser.ast.expression.single;

import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.declaration.constant.constInitVal.ConstInitValEle;
import frontend.parser.ast.expression.opExp.AddExp;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrType;
import middle.llvm_ir.utils.constant.IrConstInt;

public class ConstExp implements ConstInitValEle {
    private final SyntaxType type;
    private final AddExp addExp;

    public ConstExp(AddExp addExp) {
        this.type = SyntaxType.CONST_EXP;
        this.addExp = addExp;
    }

    @Override
    public String syntaxInfoOutput() {
        return addExp.syntaxInfoOutput() + type.getName() + "\n";
    }

    public int evaluate() {
        return addExp.evaluate();
    }

    /**
     * not use
     * @return null
     */
    @Override
    public IrValue genIR() {
        return null;
    }

    public IrConstInt genConstIR(IrType type) {
        return new IrConstInt(type, evaluate());
    }
}
