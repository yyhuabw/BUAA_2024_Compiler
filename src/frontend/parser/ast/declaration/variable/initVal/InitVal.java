package frontend.parser.ast.declaration.variable.initVal;

import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.expression.single.Exp;
import frontend.parser.ast.terminal.StringConst;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrArrayType;
import middle.llvm_ir.type.IrType;
import middle.llvm_ir.utils.constant.IrConstant;

public class InitVal implements SyntaxNode {
    private final SyntaxType type;
    private final InitValEle initValEle;

    public InitVal(InitValEle initValEle) {
        this.type = SyntaxType.INIT_VAL;
        this.initValEle = initValEle;
    }

    @Override
    public String syntaxInfoOutput() {
        return initValEle.syntaxInfoOutput() + type.getName() + "\n";
    }

    /**
     * not use
     * @return null
     */
    @Override
    public IrValue genIR() {
        return null;
    }

    public IrValue genVarIR(IrType type) {
        return initValEle.genVarIR(type);
    }

    public IrConstant genConstIR(IrType type) {
        if (initValEle instanceof Exp exp) {
            return exp.genConstIR(type);
        } else if (initValEle instanceof InitArrayVal initArrayVal) {
            return initArrayVal.genConstIR((IrArrayType) type);
        } else if (initValEle instanceof StringConst stringConst) {
            return stringConst.genConstIR((IrArrayType) type);
        }
        System.out.println("Error class in initValEle of InitVal");
        return null;
    }
}
