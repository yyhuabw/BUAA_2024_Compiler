package frontend.parser.ast.expression.single;

import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.declaration.variable.initVal.InitValEle;
import frontend.parser.ast.expression.opExp.AddExp;
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
}
