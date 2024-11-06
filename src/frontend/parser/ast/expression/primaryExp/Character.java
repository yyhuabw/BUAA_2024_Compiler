package frontend.parser.ast.expression.primaryExp;

import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.terminal.CharConst;
import middle.llvm_ir.IrValue;
import middle.symbol.value.ValueType;

public class Character implements PrimaryExpEle {
    private final SyntaxType type;
    private final CharConst charConst;

    public Character(CharConst charConst) {
        this.type = SyntaxType.CHARACTER;
        this.charConst = charConst;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.CHAR;
    }

    @Override
    public int getDim() {
        return 0;
    }

    @Override
    public String syntaxInfoOutput() {
        return charConst.syntaxInfoOutput() + type.getName() + "\n";
    }

    @Override
    public int evaluate() {
        return charConst.evaluate();
    }

    @Override
    public IrValue genIR() {
        return charConst.genIR();
    }
}
