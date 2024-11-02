package frontend.parser.ast.expression.primaryExp;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.expression.single.Exp;
import frontend.parser.ast.terminal.Ident;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.memory.IrGEPInstr;
import middle.llvm_ir.instruction.memory.IrLoadInstr;
import middle.llvm_ir.type.IrArrayType;
import middle.llvm_ir.type.IrPointerType;
import middle.llvm_ir.type.IrType;
import middle.symbol.ConstSymbol;
import middle.symbol.Symbol;
import middle.symbol.VarSymbol;
import middle.symbol.value.ValueType;

import java.util.ArrayList;

public class LVal implements PrimaryExpEle {
    private final SyntaxType type;
    private final Ident ident;
    private final ArrayList<Token> leftBrackets;
    private final ArrayList<Exp> exps;
    private final ArrayList<Token> rightBrackets;

    public LVal(Ident ident, ArrayList<Token> leftBrackets, ArrayList<Exp> exps, ArrayList<Token> rightBrackets) {
        this.type = SyntaxType.LVAL;
        this.ident = ident;
        this.leftBrackets = leftBrackets;
        this.exps = exps;
        this.rightBrackets = rightBrackets;
    }

    public boolean isConst() {
        return ident.queryIsConst();
    }

    public int getLineno() {
        return ident.getLineno();
    }

    @Override
    public ValueType getValueType() {
        return ident.queryValueType();
    }

    /**
     * not type
     * a[1]'s dim is regarded as 0
     */
    @Override
    public int getDim() {
        int dimension = ident.queryDim();
        if (dimension < 0) { // undefined ident
            return -1;
        }
        if (dimension > 0 && leftBrackets.isEmpty()) { // array
            return dimension;
        }
        return 0; // maybe a[1]
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident.syntaxInfoOutput());
        for (int i = 0; i < leftBrackets.size(); i++) {
            sb.append(leftBrackets.get(i).syntaxInfoOutput());
            sb.append(exps.get(i).syntaxInfoOutput());
            sb.append(rightBrackets.get(i).syntaxInfoOutput());
        }
        sb.append(type.getName()).append("\n");
        return sb.toString();
    }

    /**
     * one kind of PrimaryExp
     * for "... = ... LVal ..."
     */
    @Override
    public IrValue genIR() {
        Symbol symbol = ident.getSymbol();
        IrValue value = ident.genIR();
        int dim = ident.queryDim();
        int bracketNum = leftBrackets.size();

        if (symbol instanceof ConstSymbol) {
            if (dim == 0) { // no need loadInstr, directly use the value
                return value;
            } else { // dim == 1
                if (dim == bracketNum) { // x = a[1]
                    IrGEPInstr gepInstr = genGEPInstr(value);
                    return new IrLoadInstr(IrBuilder.getInstance().getLocalVarName(), gepInstr);
                }
            }
        } else if (symbol instanceof VarSymbol) {
            if (dim == 0) { // load instr
                return new IrLoadInstr(IrBuilder.getInstance().getLocalVarName(), value);
            } else { // dim == 1
                if (dim == bracketNum) { // x = a[1]
                    IrGEPInstr gepInstr = genGEPInstr(value);
                    return new IrLoadInstr(IrBuilder.getInstance().getLocalVarName(), gepInstr);
                }
            }
        }

        System.out.println("Invalid dimensions: " + dim + ", bracket count: " + bracketNum);
        return null;
    }

    /**
     * for "LVal = ..."
     * lvalue must be variable
     */
    public IrValue genIRForAssign() {
        int dim = ident.queryDim();
        IrValue value = ident.genIR();

        if (dim == 0) {
            return value;
        } else { // dim == 1, the situation of "a[1] = 1"
            return genGEPInstr(value);
        }
    }

    private IrGEPInstr genGEPInstr(IrValue value) {
        // the type of value must be the pointer of array
        IrPointerType pointerType = (IrPointerType) value.getType();
        IrArrayType arrayType = (IrArrayType) pointerType.getTargetType();
        IrType eleType = arrayType.getEleType();
        IrPointerType gepPtrType = new IrPointerType(eleType);

        String irName = IrBuilder.getInstance().getLocalVarName();

        // dim == 1, only has one exp
        IrValue irIndexValue = exps.get(0).genIR();

        return new IrGEPInstr(gepPtrType, irName, value, irIndexValue);
    }
}
