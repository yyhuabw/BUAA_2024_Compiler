package frontend.parser.ast.declaration.variable.initVal;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Exp;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.type.IrArrayType;
import middle.llvm_ir.type.IrType;
import middle.llvm_ir.utils.IrValArray;
import middle.llvm_ir.utils.constant.IrConstArray;
import middle.llvm_ir.utils.constant.IrConstInt;
import middle.llvm_ir.utils.constant.IrConstant;

import java.util.ArrayList;

public class InitArrayVal implements InitValEle {
    private final Token leftBrace;
    private Exp first = null;
    private ArrayList<Token> commas = null;
    private ArrayList<Exp> exps = null;
    private final Token rightBrace;

    public InitArrayVal(Token leftBrace, Token rightBrace) {
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
    }

    public InitArrayVal(Token leftBrace, Exp first, ArrayList<Token> commas, ArrayList<Exp> exps, Token rightBrace) {
        this(leftBrace, rightBrace);
        this.first = first;
        this.commas = commas;
        this.exps = exps;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(leftBrace.syntaxInfoOutput());
        if (first != null) {
            sb.append(first.syntaxInfoOutput());
            for (int i = 0; i < commas.size(); i++) {
                sb.append(commas.get(i).syntaxInfoOutput());
                sb.append(exps.get(i).syntaxInfoOutput());
            }
        }
        sb.append(rightBrace.syntaxInfoOutput());
        return sb.toString();
    }

    /**
     * not use
     * @return null
     */
    @Override
    public IrValue genIR() {
        return null;
    }

    // for local variable
    @Override
    public IrValue genVarIR(IrType type) {
        ArrayList<IrValue> values = new ArrayList<>();

        values.add(first.genIR());
        for (Exp exp : exps) {
            values.add(exp.genIR());
        }

        return new IrValArray(type, values);
    }

    // for global variable
    public IrConstant genConstIR(IrArrayType type) {
        IrType eleType = type.getEleType();
        ArrayList<IrConstInt> values = new ArrayList<>();

        values.add(new IrConstInt(eleType, first.evaluate()));
        for (Exp exp : exps) {
            values.add(new IrConstInt(eleType, exp.evaluate()));
        }

        return new IrConstArray(type, values);
    }
}
