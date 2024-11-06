package frontend.parser.ast.function.single;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.declaration.type.BType;
import frontend.parser.ast.expression.single.ConstExp;
import frontend.parser.ast.terminal.Ident;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.function.IrFParam;
import middle.llvm_ir.instruction.memory.IrAllocaInstr;
import middle.llvm_ir.instruction.memory.IrStoreInstr;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.type.IrPointerType;
import middle.llvm_ir.type.IrType;
import middle.symbol.SymbolManager;
import middle.symbol.VarSymbol;
import middle.symbol.value.ValueType;

import java.util.ArrayList;

// FuncFParam → BType Ident ['[' ']']
public class FuncFParam implements SyntaxNode {
    private final SyntaxType type;
    private final BType bType;
    private final Ident ident;
    private Token firstLeftBracket = null;
    private Token firstRightBracket = null;
    // multi-dimentional array
    private ArrayList<Token> leftBrackets = null;
    private ArrayList<ConstExp> constExps = null;
    private ArrayList<Token> rightBrackets = null;
    private VarSymbol varSymbol = null;

    public FuncFParam(BType bType, Ident ident) {
        this.type = SyntaxType.FUNC_FORMAL_PARAM;
        this.bType = bType;
        this.ident = ident;
    }

    public FuncFParam(BType bType,
                      Ident ident,
                      Token firstLeftBracket,
                      Token firstRightBracket) {
        this(bType, ident);
        this.firstLeftBracket = firstLeftBracket;
        this.firstRightBracket = firstRightBracket;
    }

    public FuncFParam(BType bType,
                      Ident ident,
                      Token firstLeftBracket,
                      Token firstRightBracket,
                      ArrayList<Token> leftBrackets,
                      ArrayList<ConstExp> constExps,
                      ArrayList<Token> rightBrackets) {
        this(bType, ident, firstLeftBracket, firstRightBracket);
        this.leftBrackets = leftBrackets;
        this.constExps = constExps;
        this.rightBrackets = rightBrackets;
    }

    public boolean addToSTAndCheck() {
        String name = ident.getToken().getContent();
        ValueType valueType = bType.getValueType();

        int dimension = 0;
        if (firstLeftBracket != null) {
            dimension++;
            dimension += leftBrackets.size();
        }

        VarSymbol varSymbol = new VarSymbol(name, valueType, dimension);
        this.varSymbol = varSymbol;
        return SymbolManager.getInstance().addAndCheck(varSymbol);
    }

    public VarSymbol getVarSymbol() {
        return varSymbol;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(bType.syntaxInfoOutput());
        sb.append(ident.syntaxInfoOutput());
        if (firstLeftBracket != null) {
            sb.append(firstLeftBracket.syntaxInfoOutput());
            sb.append(firstRightBracket.syntaxInfoOutput());
            for (int i = 0; i < leftBrackets.size(); i++) {
                sb.append(leftBrackets.get(i).syntaxInfoOutput());
                sb.append(constExps.get(i).syntaxInfoOutput());
                sb.append(rightBrackets.get(i).syntaxInfoOutput());
            }
        }
        sb.append(type.getName()).append("\n");
        return sb.toString();
    }

    /**
     * FuncFParam → BType Ident ['[' ']']
     * void
     * @return null
     */
    @Override
    public IrValue genIR() {
        SymbolManager.getInstance().addAndCheck(varSymbol); // must success

        IrType fParamType;
        if (varSymbol.getDim() == 0) {
            fParamType = bType.getValueType() == ValueType.INT ? IrIntType.INT32 : IrIntType.INT8;
        } else { // array
            fParamType = bType.getValueType() == ValueType.INT ? new IrPointerType(IrIntType.INT32) : new IrPointerType(IrIntType.INT8);
        }
        IrFParam fParam = new IrFParam(fParamType, IrBuilder.getInstance().getFuncParamName());

        if (varSymbol.getDim() == 0) {
            IrAllocaInstr allocaInstr = new IrAllocaInstr(IrBuilder.getInstance().getLocalVarName(), fParamType);
            new IrStoreInstr(fParam, allocaInstr);
            varSymbol.setIrValue(allocaInstr);
        } else { // param's type is pointer
            varSymbol.setIrValue(fParam);
        }

        return null;
    }
}
