package frontend.parser.ast.expression.single;

import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.declaration.variable.initVal.InitValEle;
import frontend.parser.ast.expression.opExp.AddExp;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.type_change.IrTruncInstr;
import middle.llvm_ir.instruction.type_change.IrZextInstr;
import middle.llvm_ir.type.IrIntType;
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

    /**
     * int a = exp;
     * char a = exp;
     */
    @Override
    public IrValue genVarIR(IrType type) {
        IrValue expIR = genIR();

        if (type.isINT32() && expIR.getType().isINT8()) { // INT8 should change to INT32
            if (expIR instanceof IrConstInt constInt) {
                return new IrConstInt(type, constInt.getValue());
            } else {
                return new IrZextInstr(IrIntType.INT32, IrBuilder.getInstance().getLocalVarName(), expIR);
            }
        } else if (type.isINT8() && expIR.getType().isINT32()) { // INT32 should change to INT8
            if (expIR instanceof IrConstInt constInt) {
                return new IrConstInt(type, constInt.getValue());
            } else {
                return new IrTruncInstr(IrIntType.INT8, IrBuilder.getInstance().getLocalVarName(), expIR);
            }
        } else {
            return expIR;
        }
    }

    public IrConstInt genConstIR(IrType type) {
        return new IrConstInt(type, evaluate());
    }
}
