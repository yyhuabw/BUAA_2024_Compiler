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
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.type.IrPointerType;
import middle.llvm_ir.type.IrType;
import middle.llvm_ir.utils.constant.IrConstArray;
import middle.llvm_ir.utils.constant.IrConstInt;
import middle.llvm_ir.utils.constant.IrConstant;
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
     * the LVal must be constant
     * @return int
     */
    @Override
    public int evaluate() {
        Symbol symbol = ident.getSymbol();
        if (symbol instanceof ConstSymbol constSymbol) {
            int dimension = constSymbol.getDim();
            IrConstant initValue = constSymbol.getInitValue();
            if (dimension == 0) { // int
                return ((IrConstInt) initValue).getValue();
            } else { // array, must be a[i]
                int index = exps.get(0).evaluate();
                return ((IrConstArray) initValue).getIndexValue(index);
            }
        }
        System.out.println("Error in LVal when evaluate");
        return 0;
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

        if (symbol instanceof ConstSymbol constSymbol && dim == 0) { // contant
            // no need loadInstr, directly use the value
            IrValue initValue =  constSymbol.getInitValue();
            if (initValue.getType().isINT32()) {
                return initValue;
            } else { // i8
                IrConstInt initConstVal = (IrConstInt) initValue;
                return new IrConstInt(IrIntType.INT32, initConstVal.getValue());
            }
        } else if (symbol instanceof VarSymbol && dim == 0) { // variable, x = a
            // load instr
            return new IrLoadInstr(IrBuilder.getInstance().getLocalVarName(), value);
        }

        if (dim == 1 && bracketNum == 1) { // x = a[1]
            IrGEPInstr gepInstr = genGEPInstr(value, true);
            return new IrLoadInstr(IrBuilder.getInstance().getLocalVarName(), gepInstr);
        } else if (dim == 1 && bracketNum == 0) { // foo(int a[]), x = foo(a)
            return genGEPInstr(value, false);
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
            return genGEPInstr(value, true);
        }
    }

    private IrGEPInstr genGEPInstr(IrValue value, boolean needIndex) {
        // the type of value must be the pointer of array
        IrPointerType pointerType = (IrPointerType) value.getType();
        IrArrayType arrayType = (IrArrayType) pointerType.getTargetType();
        IrType eleType = arrayType.getEleType();
        IrPointerType gepPtrType = new IrPointerType(eleType);

        String irName = IrBuilder.getInstance().getLocalVarName();

        IrValue irIndexValue;
        if (needIndex) {
            // dim == 1, only has one exp, the type should be INT32
            irIndexValue = exps.get(0).genIR();
        } else {
            irIndexValue = new IrConstInt(IrIntType.INT32, 0);
        }

        return new IrGEPInstr(gepPtrType, irName, value, irIndexValue);
    }
}
