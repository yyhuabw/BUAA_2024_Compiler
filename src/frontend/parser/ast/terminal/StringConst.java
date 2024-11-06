package frontend.parser.ast.terminal;

import frontend.lexer.token.Token;
import frontend.parser.ast.declaration.constant.constInitVal.ConstInitValEle;
import frontend.parser.ast.declaration.variable.initVal.InitValEle;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrArrayType;
import middle.llvm_ir.type.IrType;
import middle.llvm_ir.utils.constant.IrConstArray;
import middle.llvm_ir.utils.constant.IrConstInt;

import java.util.ArrayList;

public class StringConst implements ConstInitValEle, InitValEle {
    private final Token token;

    public StringConst(Token token) {
        this.token = token;
    }

    public String getContent() {
        return token.getContent();
    }

    public String getFixedContent() {
        return token.getContent().substring(1, token.getContent().length() - 1);
    }

    @Override
    public String syntaxInfoOutput() {
        return token.syntaxInfoOutput();
    }

    /**
     * not use
     * @return null
     */
    @Override
    public IrValue genIR() {
        return null;
    }

    @Override
    public IrValue genVarIR(IrType type) {
        return genConstIR((IrArrayType) type);
    }

    /**
     * except printf's stringConst
     */
    public IrConstArray genConstIR(IrArrayType type) {
        ArrayList<IrConstInt> values = new ArrayList<>();
        IrType eleType = type.getEleType(); // should be INT8

        String fixedContent = token.getContent().substring(1, token.getContent().length() - 1);
        for (int i = 0; i < fixedContent.length(); i++) {
            values.add(new IrConstInt(eleType, fixedContent.charAt(i)));
        }

        return new IrConstArray(type, values);
    }
}
