package frontend.parser.ast.function.single;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.function.funcType.FuncType;
import frontend.parser.ast.statement.block.Block;
import frontend.parser.ast.statement.block.BlockItem;
import frontend.parser.ast.statement.block.BlockItemEle;
import frontend.parser.ast.statement.stmt.ReturnStmt;
import frontend.parser.ast.statement.stmt.Stmt;
import frontend.parser.ast.terminal.Ident;
import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.instruction.jump.IrRetInstr;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.type.IrType;
import middle.llvm_ir.type.IrVoidType;
import middle.symbol.FuncSymbol;
import middle.symbol.SymbolManager;
import middle.symbol.value.ValueType;

import java.util.ArrayList;

public class FuncDef implements SyntaxNode {
    private final SyntaxType type;
    private final FuncType funcType;
    private final Ident ident;
    private Token leftParent;
    private FuncFParams funcFParams = null;
    private Token rightParent;
    private Block block;
    private FuncSymbol funcSymbol = null;

    // FuncType Ident '(' [FuncFParams] ')' Block
    public FuncDef(FuncType funcType, Ident ident) {
        this.type = SyntaxType.FUNC_DEF;
        this.funcType = funcType;
        this.ident = ident;
    }

    public void setAttributes(Token leftParent, Token rightParent, Block block) {
        this.leftParent = leftParent;
        this.rightParent = rightParent;
        this.block = block;
    }

    public void setFuncFParams(FuncFParams funcFParams) {
        this.funcFParams = funcFParams;
        funcSymbol.setParamSymbols(funcFParams.getSymbols());
    }

    public boolean addToSTAndCheck() {
        String name = ident.getToken().getContent();
        ValueType returnType = funcType.getReturnType();
        FuncSymbol funcSymbol = new FuncSymbol(name, returnType);
        this.funcSymbol = funcSymbol;
        return SymbolManager.getInstance().addAndCheck(funcSymbol);
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(funcType.syntaxInfoOutput());
        sb.append(ident.syntaxInfoOutput());
        sb.append(leftParent.syntaxInfoOutput());
        if (funcFParams != null) {
            sb.append(funcFParams.syntaxInfoOutput());
        }
        sb.append(rightParent.syntaxInfoOutput());
        sb.append(block.syntaxInfoOutput());
        sb.append(type.getName()).append("\n");
        return sb.toString();
    }

    /**
     * FuncType Ident '(' [FuncFParams] ')' Block
     * void
     * @return null
     */
    @Override
    public IrValue genIR() {
        SymbolManager.getInstance().setGlobalStatus(false);
        SymbolManager.getInstance().addAndCheck(funcSymbol); // must success
        SymbolManager.getInstance().enterFuncDef(funcSymbol);

        // create irFunction
        String name = IrBuilder.getInstance().getFuncName(ident.getToken().getContent());
        IrType irReturnType;
        irReturnType = switch (funcType.getReturnType()) {
            case INT -> IrIntType.INT32;
            case CHAR -> IrIntType.INT8;
            case VOID -> IrVoidType.VOID;
        };
        IrFunction function = new IrFunction(name, irReturnType);
        funcSymbol.setIrFunction(function);

        IrBuilder.getInstance().setCurFunction(function);

        IrBasicBlock basicBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());
        IrBuilder.getInstance().setCurBlock(basicBlock);

        if (funcFParams != null) {
            funcFParams.genIR();
        }
        block.genIR();
        checkRetStmt();

        SymbolManager.getInstance().leaveFuncDef();

        return null;
    }

    private void checkRetStmt() {
        if (funcType.getReturnType() == ValueType.VOID) {
            ArrayList<BlockItem> blockItems = block.getBlockItems();
            if (!blockItems.isEmpty()) {
                BlockItemEle lastBlockItemEle = blockItems.get(blockItems.size() - 1).getBlockItemEle();
                if (lastBlockItemEle instanceof Stmt stmt) {
                    if (stmt.getStmtEle() instanceof ReturnStmt) {
                        return;
                    }
                }
            }
            new IrRetInstr(null);
        }
    }
}
