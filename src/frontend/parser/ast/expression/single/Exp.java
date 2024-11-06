package frontend.parser.ast.expression.single;

import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.declaration.variable.initVal.InitValEle;
import frontend.parser.ast.expression.opExp.AddExp;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrType;
import middle.llvm_ir.utils.constant.IrConstInt;
import middle.symbol.value.ValueType;

public class Exp implements InitValEle {
    private final SyntaxType type;
    private final AddExp addExp;

    public Exp(AddExp addExp) {
        this.type = SyntaxType.EXP;
        this.addExp = addExp;
    }

    public ValueType getValueType() {
        return addExp.getValueType();
    }

    public int getDim() {
        return addExp.getDim();
    }

    @Override
    public String syntaxInfoOutput() {
        return addExp.syntaxInfoOutput() + type.getName() + "\n";
    }

    public int evaluate() {
        return addExp.evaluate();
    }

    @Override
    public IrValue genIR() {
        return addExp.genIR();
    }

    @Override
    public IrValue genVarIR(IrType type) {
        return genIR();
    }

    public IrConstInt genConstIR(IrType type) {
        return new IrConstInt(type, evaluate());
    }
}
