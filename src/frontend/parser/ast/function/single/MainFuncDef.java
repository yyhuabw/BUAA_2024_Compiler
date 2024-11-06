package frontend.parser.ast.function.single;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.statement.block.Block;
import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.function.IrFunction;
import middle.llvm_ir.type.IrIntType;
import middle.symbol.FuncSymbol;
import middle.symbol.SymbolManager;

public class MainFuncDef implements SyntaxNode {
    private final SyntaxType type;
    private final Token intTk;
    private final Token mainTk;
    private final Token leftParent;
    private final Token rightParent;
    private final Block block;
    private FuncSymbol mainFuncSymbol = null;

    public MainFuncDef(Token intTk,
                       Token mainTk,
                       Token leftParent,
                       Token rightParent,
                       Block block) {
        this.type = SyntaxType.MAIN_FUNC_DEF;
        this.intTk = intTk;
        this.mainTk = mainTk;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
        this.block = block;
    }

    public void setMainFuncSymbol(FuncSymbol mainFuncSymbol) {
        this.mainFuncSymbol = mainFuncSymbol;
    }

    @Override
    public String syntaxInfoOutput() {
        return intTk.syntaxInfoOutput() +
                mainTk.syntaxInfoOutput() +
                leftParent.syntaxInfoOutput() +
                rightParent.syntaxInfoOutput() +
                block.syntaxInfoOutput() +
                type.getName() + "\n";
    }

    // void
    @Override
    public IrValue genIR() {
        SymbolManager.getInstance().setGlobalStatus(false);
        SymbolManager.getInstance().addAndCheck(mainFuncSymbol);
        SymbolManager.getInstance().enterFuncDef(mainFuncSymbol);

        IrFunction irMainFunc = new IrFunction(IrBuilder.getInstance().getFuncName("main"), IrIntType.INT32);
        mainFuncSymbol.setIrFunction(irMainFunc);

        IrBuilder.getInstance().setCurFunction(irMainFunc);

        IrBasicBlock basicBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());
        IrBuilder.getInstance().setCurBlock(basicBlock);

        block.genIR();

        SymbolManager.getInstance().leaveFuncDef();

        return null;
    }
}
