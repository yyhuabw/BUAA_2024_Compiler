package frontend.parser.ast.declaration.constant.constInitVal;

import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.expression.single.ConstExp;
import frontend.parser.ast.terminal.StringConst;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrArrayType;
import middle.llvm_ir.type.IrType;
import middle.llvm_ir.utils.constant.IrConstant;

public class ConstInitVal implements SyntaxNode {
    private final SyntaxType type;
    private final ConstInitValEle constInitValEle;

    public ConstInitVal(ConstInitValEle constInitValEle) {
        this.type = SyntaxType.CONST_INITVAL;
        this.constInitValEle = constInitValEle;
    }

    @Override
    public String syntaxInfoOutput() {
        return constInitValEle.syntaxInfoOutput() + type.getName() + "\n";
    }

    /**
     * not use
     * @return null
     */
    @Override
    public IrValue genIR() {
        return null;
    }

    public IrConstant genConstIR(IrType type) {
        if (constInitValEle instanceof ConstExp constExp) {
            return constExp.genConstIR(type);
        } else if (constInitValEle instanceof ConstArrayInitVal constArrayInitVal) {
            return constArrayInitVal.genConstIR((IrArrayType) type);
        } else if (constInitValEle instanceof StringConst stringConst) {
            return stringConst.genConstIR((IrArrayType) type);
        }
        System.out.println("Error class in constInitValEle of ConstInitVal");
        return null;
    }
}
