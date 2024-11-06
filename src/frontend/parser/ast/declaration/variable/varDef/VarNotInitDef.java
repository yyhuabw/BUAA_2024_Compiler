package frontend.parser.ast.declaration.variable.varDef;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.ConstExp;
import frontend.parser.ast.terminal.Ident;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.memory.IrAllocaInstr;
import middle.llvm_ir.type.IrArrayType;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.type.IrPointerType;
import middle.llvm_ir.type.IrType;
import middle.llvm_ir.utils.IrGlobalVar;
import middle.llvm_ir.utils.constant.IrConstArray;
import middle.llvm_ir.utils.constant.IrConstInt;
import middle.llvm_ir.utils.constant.IrConstant;
import middle.symbol.SymbolManager;
import middle.symbol.VarSymbol;
import middle.symbol.value.ValueType;

import java.util.ArrayList;

public class VarNotInitDef implements VarDefEle {
    private final Ident ident;
    private ArrayList<Token> leftBrackets = null;
    private ArrayList<ConstExp> constExps = null;
    private ArrayList<Token> rightBrackets = null;
    private VarSymbol varSymbol = null;

    public VarNotInitDef(Ident ident) {
        this.ident = ident;
    }

    public VarNotInitDef(Ident ident, ArrayList<Token> leftBrackets, ArrayList<ConstExp> constExps, ArrayList<Token> rightBrackets) {
        this(ident);
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

    private void globalSetInit(IrType irType) {
        IrConstant irConstant;
        if (varSymbol.getDim() == 0) {
            irConstant = new IrConstInt(irType, 0);
        } else { // array
            irConstant = new IrConstArray(irType, new ArrayList<>());
        }
        varSymbol.setInitValue(irConstant);
    }

    @Override
    public IrValue genIR() {
        IrType initValType = getInitValType(varSymbol.getValueType());

        SymbolManager.getInstance().addAndCheck(varSymbol); // must success

        if (varSymbol.isGlobal()) { // global variable
            globalSetInit(initValType);

            IrConstant initValue = varSymbol.getInitValue();
            IrPointerType irType = new IrPointerType(initValue.getType());
            String name = IrBuilder.getInstance().getGlobalVarName(ident.getToken().getContent());
            varSymbol.setIrValue(new IrGlobalVar(irType, name, initValue));
        } else { // local variable
            IrAllocaInstr allocaInstr = new IrAllocaInstr(IrBuilder.getInstance().getLocalVarName(), initValType);
            varSymbol.setIrValue(allocaInstr);
        }

        return null; // void
    }
}
