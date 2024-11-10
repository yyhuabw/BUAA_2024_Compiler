package frontend.parser.ast.declaration.variable.varDef;

import frontend.lexer.token.Token;
import frontend.parser.ast.declaration.variable.initVal.InitVal;
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
import middle.llvm_ir.utils.IrValArray;
import middle.llvm_ir.utils.constant.IrConstArray;
import middle.llvm_ir.utils.constant.IrConstInt;
import middle.llvm_ir.utils.constant.IrConstant;
import middle.symbol.SymbolManager;
import middle.symbol.VarSymbol;
import middle.symbol.value.ValueType;

import java.util.ArrayList;

public class VarInitDef implements VarDefEle {
    private final Ident ident;
    private ArrayList<Token> leftBrackets = null;
    private ArrayList<ConstExp> constExps = null;
    private ArrayList<Token> rightBrackets = null;
    private final Token assign;
    private final InitVal initVal;
    private VarSymbol varSymbol = null;

    public VarInitDef(Ident ident, Token assign, InitVal initVal) {
        this.ident = ident;
        this.assign = assign;
        this.initVal = initVal;
    }

    public VarInitDef(Ident ident, ArrayList<Token> leftBrackets, ArrayList<ConstExp> constExps, ArrayList<Token> rightBrackets, Token assign, InitVal initVal) {
        this(ident, assign, initVal);
        this.leftBrackets = leftBrackets;
        this.constExps = constExps;
        this.rightBrackets = rightBrackets;
    }

    public boolean addToSTAndCheck(ValueType valueType) {
        String name = ident.getToken().getContent();
        int dimension = leftBrackets.size();
        this.varSymbol = new VarSymbol(name, valueType, dimension);
        return SymbolManager.getInstance().addAndCheck(varSymbol);
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
        sb.append(initVal.syntaxInfoOutput());
        return sb.toString();
    }

    private IrType getInitValType(ValueType valueType) {
        int dimension = leftBrackets.size();
        IrType initValType;
        if (dimension == 0) {
            initValType = valueType == ValueType.INT ? IrIntType.INT32 : IrIntType.INT8;
        } else { // one-dimensional array
            int eleNum = constExps.get(0).evaluate(); // only have one-dimensional
            initValType = valueType == ValueType.INT ? new IrArrayType(IrIntType.INT32, eleNum) : new IrArrayType(IrIntType.INT8, eleNum);
        }
        return initValType;
    }

    @Override
    public IrValue genIR() {
        IrType initValType = getInitValType(varSymbol.getValueType());

        SymbolManager.getInstance().addAndCheck(varSymbol); // must success

        if (varSymbol.isGlobal()) { // only global variable can use "varSymbol.getInitValue()"
            varSymbol.setInitValue(initVal.genConstIR(initValType));

            IrConstant initValue = varSymbol.getInitValue();
            IrPointerType irType = new IrPointerType(initValue.getType());
            String name = IrBuilder.getInstance().getGlobalVarName(ident.getToken().getContent());
            varSymbol.setIrValue(new IrGlobalVar(irType, name, initValue));
        } else { // local variable
            IrAllocaInstr allocaInstr = new IrAllocaInstr(IrBuilder.getInstance().getLocalVarName(), initValType);
            varSymbol.setIrValue(allocaInstr);
            IrValue varValue = initVal.genVarIR(initValType);

            if (varSymbol.getDim() == 0) { // "char" or "int"
                new IrStoreInstr(varValue, allocaInstr);
            } else { // array
                if (varValue instanceof IrValArray irValArray) { // InitArrayVal
                    ArrayList<IrValue> values = irValArray.getValues();
                    IrInstruction instruction;
                    int index = 0;
                    for (IrValue irValue : values) {
                        IrType eleType = ((IrArrayType) initValType).getEleType();
                        IrPointerType gepPtrType = new IrPointerType(eleType);

                        instruction = new IrGEPInstr(gepPtrType, IrBuilder.getInstance().getLocalVarName(), allocaInstr, new IrConstInt(IrIntType.INT32, index));
                        new IrStoreInstr(irValue, instruction);

                        index++;
                    }
                } else if (varValue instanceof IrConstArray irConstArray) { // StringConst
                    ArrayList<IrConstInt> values = irConstArray.getValues();
                    IrInstruction instruction;
                    int index = 0;
                    for (IrConstInt irConstInt : values) {
                        IrType eleType = ((IrArrayType) initValType).getEleType();
                        IrPointerType gepPtrType = new IrPointerType(eleType);

                        instruction = new IrGEPInstr(gepPtrType, IrBuilder.getInstance().getLocalVarName(), allocaInstr, new IrConstInt(IrIntType.INT32, index));
                        new IrStoreInstr(irConstInt, instruction);

                        index++;
                    }
                } else {
                    System.out.println("Error class of varValue in VarInitDef");
                }
            }
        }

        return null; // void
    }
}
