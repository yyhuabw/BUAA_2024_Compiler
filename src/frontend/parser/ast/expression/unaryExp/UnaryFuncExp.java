package frontend.parser.ast.expression.unaryExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Exp;
import frontend.parser.ast.expression.single.FuncRParams;
import frontend.parser.ast.terminal.Ident;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.jump.call.IrCallValInstr;
import middle.llvm_ir.instruction.jump.call.IrCallVoidInstr;
import middle.llvm_ir.instruction.type_change.IrTruncInstr;
import middle.llvm_ir.instruction.type_change.IrZextInstr;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.utils.constant.IrConstInt;
import middle.symbol.FuncSymbol;
import middle.symbol.VarSymbol;
import middle.symbol.value.ValueType;

import java.util.ArrayList;

public class UnaryFuncExp implements UnaryExpEle {
    private final Ident ident;
    private final Token leftParent;
    private FuncRParams funcRParams = null;
    private final Token rightParent;

    public UnaryFuncExp(Ident ident, Token leftParent, Token rightParent) {
        this.ident = ident;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
    }

    public UnaryFuncExp(Ident ident, Token leftParent, FuncRParams funcRParams, Token rightParent) {
        this(ident, leftParent, rightParent);
        this.funcRParams = funcRParams;
    }

    @Override
    public ValueType getValueType() {
        return ident.queryValueType();
    }

    @Override
    public int getDim() {
        return ident.queryDim();
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident.syntaxInfoOutput());
        sb.append(leftParent.syntaxInfoOutput());
        if (funcRParams != null) {
            sb.append(funcRParams.syntaxInfoOutput());
        }
        sb.append(rightParent.syntaxInfoOutput());
        return sb.toString();
    }

    /**
     * not use
     * @return 0
     */
    @Override
    public int evaluate() {
        return 0;
    }

    @Override
    public IrValue genIR() {
        IrFunction irFunction = (IrFunction) ident.genIR();

        ArrayList<IrValue> params = new ArrayList<>();
        if (funcRParams != null) {
            ArrayList<Exp> exps = funcRParams.getAllExps();
            ArrayList<VarSymbol> paramSymbols = ((FuncSymbol) ident.getSymbol()).getParamSymbols();
            for (int i = 0; i < exps.size(); i++) {
                IrValue expIR = exps.get(i).genIR();
                VarSymbol paramSymbol = paramSymbols.get(i);

                // type change
                if (paramSymbol.getValueType() == ValueType.CHAR && expIR.getType().isINT32()) {
                    if (expIR instanceof IrConstInt constInt) {
                        return new IrConstInt(IrIntType.INT8, constInt.getValue());
                    } else {
                        expIR = new IrTruncInstr(IrIntType.INT8, IrBuilder.getInstance().getLocalVarName(), expIR);
                    }
                } else if (paramSymbol.getValueType() == ValueType.INT && expIR.getType().isINT8()) {
                    if (expIR instanceof IrConstInt constInt) {
                        return new IrConstInt(IrIntType.INT32, constInt.getValue());
                    } else {
                        expIR = new IrZextInstr(IrIntType.INT32, IrBuilder.getInstance().getLocalVarName(), expIR);
                    }
                }

                params.add(expIR);
            }
        }

        if (irFunction.isVoid()) {
            return new IrCallVoidInstr(irFunction, params);
        } else {
            return new IrCallValInstr(IrBuilder.getInstance().getLocalVarName(), irFunction, params);
        }
    }
}
