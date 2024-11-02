package frontend.parser.ast.expression.primaryExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Exp;
import middle.llvm_ir.IrValue;
import middle.symbol.value.ValueType;

public class ParentExp implements PrimaryExpEle {
    private final Token leftParent;
    private final Exp exp;
    private final Token rightParent;

    public ParentExp(Token leftParent, Exp exp, Token rightParent) {
        this.leftParent = leftParent;
        this.exp = exp;
        this.rightParent = rightParent;
    }

    @Override
    public ValueType getValueType() {
        return exp.getValueType();
    }

    @Override
    public int getDim() {
        return exp.getDim();
    }

    @Override
    public String syntaxInfoOutput() {
        return leftParent.syntaxInfoOutput() + exp.syntaxInfoOutput() + rightParent.syntaxInfoOutput();
    }

    @Override
    public IrValue genIR() {
        return exp.genIR();
    }
}
