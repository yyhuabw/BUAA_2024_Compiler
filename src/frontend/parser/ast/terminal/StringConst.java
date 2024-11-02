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

    public IrConstArray genIR(IrArrayType type) {
        ArrayList<IrConstInt> values = new ArrayList<>();

        IrType eleType = type.getEleType();
        int eleNum = type.getEleNum();
        String content = token.getContent();

        for (int i = 0; i < eleNum; i++) {
            int value;
            if (i < content.length()) {
                value = content.charAt(i);
            } else {
                value = 0;
            }
            values.add(new IrConstInt(eleType, value));
        }

        return new IrConstArray(type, values);
    }
}
