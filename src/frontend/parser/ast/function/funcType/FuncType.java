package frontend.parser.ast.function.funcType;

import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.declaration.type.CharType;
import frontend.parser.ast.declaration.type.IntType;
import frontend.parser.ast.declaration.type.VoidType;
import middle.symbol.value.ValueType;

public class FuncType implements SyntaxNode {
    private final SyntaxType type;
    private final FuncTypeEle funcTypeEle;

    public FuncType(FuncTypeEle funcTypeEle) {
        this.type = SyntaxType.FUNC_TYPE;
        this.funcTypeEle = funcTypeEle;
    }

    public ValueType getReturnType() { // for symbol
        if (funcTypeEle instanceof CharType) {
            return ValueType.CHAR;
        } else if (funcTypeEle instanceof IntType) {
            return ValueType.INT;
        } else if (funcTypeEle instanceof VoidType) {
            return ValueType.VOID;
        }
        return null;
    }

    @Override
    public String syntaxInfoOutput() {
        return funcTypeEle.syntaxInfoOutput() + type.getName() + "\n";
    }
}
