package frontend.parser.ast.declaration.constant;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.declaration.constant.constInitVal.ConstInitVal;
import frontend.parser.ast.expression.single.ConstExp;
import frontend.parser.ast.terminal.Ident;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.IrInstruction;
import middle.llvm_ir.instruction.memory.IrAllocaInstr;
import middle.llvm_ir.instruction.memory.IrGEPInstr;
import middle.llvm_ir.instruction.memory.IrStoreInstr;
import middle.llvm_ir.type.IrArrayType;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.type.IrPointerType;
import middle.llvm_ir.type.IrType;
import middle.llvm_ir.utils.IrGlobalVar;
import middle.llvm_ir.utils.constant.IrConstArray;
import middle.llvm_ir.utils.constant.IrConstInt;
import middle.llvm_ir.utils.constant.IrConstant;
import middle.symbol.ConstSymbol;
import middle.symbol.SymbolManager;
import middle.symbol.value.ValueType;

import java.util.ArrayList;

public class ConstDef implements SyntaxNode {
    private final SyntaxType type;
    private final Ident ident;
    private ArrayList<Token> leftBrackets = null;
    private ArrayList<ConstExp> constExps = null;
    private ArrayList<Token> rightBrackets = null;

    private final Token assign;
    private final ConstInitVal constInitVal;
    private ConstSymbol constSymbol = null;

    public ConstDef(Ident ident, Token assign, ConstInitVal constInitVal) {
        this.type = SyntaxType.CONST_DEF;
        this.ident = ident;
        this.assign = assign;
        this.constInitVal = constInitVal;
    }

    public ConstDef(Ident ident, ArrayList<Token> leftBrackets, ArrayList<ConstExp> constExps, ArrayList<Token> rightBrackets, Token assign, ConstInitVal constInitVal) {
        this(ident, assign, constInitVal);
        this.leftBrackets = leftBrackets;
        this.constExps = constExps;
        this.rightBrackets = rightBrackets;
    }

    public boolean addToSTAndCheck(ValueType valueType) {
        String name = ident.getToken().getContent();
        int dimension = leftBrackets.size();
        this.constSymbol = new ConstSymbol(name, valueType, dimension);
        return SymbolManager.getInstance().addAndCheck(constSymbol);
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident.syntaxInfoOutput());
        for (int i = 0; i < leftBrackets.size(); i++) {
            sb.append(leftBrackets.get(i).syntaxInfoOutput());
            sb.append(constExps.get(i).syntaxInfoOutput());
            sb.append(rightBrackets.get(i).syntaxInfoOutput());
        }
        sb.append(assign.syntaxInfoOutput());
        sb.append(constInitVal.syntaxInfoOutput());
        sb.append(type.getName()).append("\n");
        return sb.toString();
    }

    private void symbolSetInitVal(ValueType valueType) {
        int dimension = leftBrackets.size();

        IrType initValType;
        if (dimension == 0) {
            initValType = valueType == ValueType.INT ? IrIntType.INT32 : IrIntType.INT8;
        } else { // one-dimensional array
            int eleNum = constExps.get(0).evaluate(); // only have one-dimensional
            initValType = valueType == ValueType.INT ? new IrArrayType(IrIntType.INT32, eleNum) : new IrArrayType(IrIntType.INT8, eleNum);
        }
        IrConstant initValue = constInitVal.genConstIR(initValType);

        constSymbol.setInitValue(initValue);
    }

    /**
     * @return null -> void
     */
    @Override
    public IrValue genIR() {
        symbolSetInitVal(constSymbol.getValueType());

        SymbolManager.getInstance().addAndCheck(constSymbol); // must success

        IrConstant initValue = constSymbol.getInitValue();

        if (constSymbol.isGlobal()) { // global constant
            IrPointerType irType = new IrPointerType(initValue.getType());
            String name = IrBuilder.getInstance().getGlobalVarName(ident.getToken().getContent());
            constSymbol.setIrValue(new IrGlobalVar(irType, name, initValue));
        } else { // local constant
            IrType irType = initValue.getType();
            IrAllocaInstr allocaInstr = new IrAllocaInstr(IrBuilder.getInstance().getLocalVarName(), irType);
            constSymbol.setIrValue(allocaInstr);

            if (constSymbol.getDim() == 0) {
                new IrStoreInstr(initValue, allocaInstr);
            } else { // array
                ArrayList<IrConstInt> values = ((IrConstArray) initValue).getValues();
                IrInstruction instruction;
                int index = 0;
                for (IrConstInt irConstInt : values) {
                    IrType eleType = ((IrArrayType) irType).getEleType();
                    IrPointerType gepPtrType = new IrPointerType(eleType);

                    instruction = new IrGEPInstr(gepPtrType, IrBuilder.getInstance().getLocalVarName(), allocaInstr, new IrConstInt(IrIntType.INT32, index));
                    new IrStoreInstr(irConstInt, instruction);

                    index++;
                }
            }
        }

        return null; // void
    }
}
