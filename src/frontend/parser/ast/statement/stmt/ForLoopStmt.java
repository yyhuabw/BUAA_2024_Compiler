package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Cond;
import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.jump.br.IrDirtBrInstr;
import middle.llvm_ir.utils.IrForLoop;
import middle.symbol.SymbolManager;

// // 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
public class ForLoopStmt implements StmtEle {
    private final Token forTk;
    private final Token leftParent;
    private ForStmt forStmt1 = null;
    private final Token semicolon1;
    private Cond cond = null;
    private final Token semicolon2;
    private ForStmt forStmt2 = null;
    private final Token rightParent;
    private final Stmt stmt;

    public ForLoopStmt(Token forTk,
                       Token leftParent,
                       Token semicolon1,
                       Token semicolon2,
                       Token rightParent,
                       Stmt stmt) {
        this.forTk = forTk;
        this.leftParent = leftParent;
        this.semicolon1 = semicolon1;
        this.semicolon2 = semicolon2;
        this.rightParent = rightParent;
        this.stmt = stmt;
    }

    public ForLoopStmt(Token forTk,
                       Token leftParent,
                       ForStmt forStmt1,
                       Token semicolon1,
                       Cond cond,
                       Token semicolon2,
                       ForStmt forStmt2,
                       Token rightParent,
                       Stmt stmt) {
        this(forTk, leftParent, semicolon1, semicolon2, rightParent, stmt);
        this.forStmt1 = forStmt1;
        this.cond = cond;
        this.forStmt2 = forStmt2;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(forTk.syntaxInfoOutput());
        sb.append(leftParent.syntaxInfoOutput());
        if (forStmt1 != null) {
            sb.append(forStmt1.syntaxInfoOutput());
        }
        sb.append(semicolon1.syntaxInfoOutput());
        if (cond != null) {
            sb.append(cond.syntaxInfoOutput());
        }
        sb.append(semicolon2.syntaxInfoOutput());
        if (forStmt2 != null) {
            sb.append(forStmt2.syntaxInfoOutput());
        }
        sb.append(rightParent.syntaxInfoOutput());
        sb.append(stmt.syntaxInfoOutput());
        return sb.toString();
    }

    /**
     * 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
     * init -> cond -> stmt -> step -> cond ...
     * void
     * @return null
     */
    @Override
    public IrValue genIR() {
        SymbolManager.getInstance().enterLoop();

        // init genIR
        if (forStmt1 != null) {
            forStmt1.genIR();
        }

        if (cond != null && forStmt2 != null) {
            condAndStep();
        } else if (cond == null && forStmt2 != null) {
            onlyStep();
        } else if (cond != null) { // forStmt2 == null
            onlyCond();
        } else { // cond == null && forStmt2 == null
            noCondNoStep();
        }

        SymbolManager.getInstance().leaveLoop();

        return null;
    }

    private void condAndStep() {
        // create basicBlock
        IrBasicBlock condBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());
        IrBasicBlock loopBodyBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());
        IrBasicBlock stepBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());
        IrBasicBlock followBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());

        IrBuilder.getInstance().pushLoop(new IrForLoop(stepBlock, followBlock));

        new IrDirtBrInstr(condBlock);

        IrBuilder.getInstance().setCurBlock(condBlock);
        cond.genIRForCond(loopBodyBlock, followBlock);

        IrBuilder.getInstance().setCurBlock(loopBodyBlock);
        stmt.genIR();

        new IrDirtBrInstr(stepBlock);

        IrBuilder.getInstance().setCurBlock(stepBlock);
        forStmt2.genIR();

        new IrDirtBrInstr(condBlock);

        IrBuilder.getInstance().popLoop();

        IrBuilder.getInstance().setCurBlock(followBlock);
    }

    private void onlyStep() {
        // create basicBlock
        IrBasicBlock loopBodyBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());
        IrBasicBlock stepBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());
        IrBasicBlock followBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());

        IrBuilder.getInstance().pushLoop(new IrForLoop(stepBlock, followBlock));

        IrBuilder.getInstance().setCurBlock(loopBodyBlock);
        stmt.genIR();

        new IrDirtBrInstr(stepBlock);

        IrBuilder.getInstance().setCurBlock(stepBlock);
        forStmt2.genIR();

        new IrDirtBrInstr(loopBodyBlock);

        IrBuilder.getInstance().popLoop();

        IrBuilder.getInstance().setCurBlock(followBlock);
    }

    private void onlyCond() {
        // create basicBlock
        IrBasicBlock condBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());
        IrBasicBlock loopBodyBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());
        IrBasicBlock followBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());

        IrBuilder.getInstance().pushLoop(new IrForLoop(condBlock, followBlock));

        new IrDirtBrInstr(condBlock);

        IrBuilder.getInstance().setCurBlock(condBlock);
        cond.genIRForCond(loopBodyBlock, followBlock);

        IrBuilder.getInstance().setCurBlock(loopBodyBlock);
        stmt.genIR();

        new IrDirtBrInstr(condBlock);

        IrBuilder.getInstance().popLoop();

        IrBuilder.getInstance().setCurBlock(followBlock);
    }

    private void noCondNoStep() {
        // create basicBlock
        IrBasicBlock loopBodyBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());
        IrBasicBlock followBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());

        IrBuilder.getInstance().pushLoop(new IrForLoop(loopBodyBlock, followBlock));

        IrBuilder.getInstance().setCurBlock(loopBodyBlock);
        stmt.genIR();

        new IrDirtBrInstr(loopBodyBlock);

        IrBuilder.getInstance().popLoop();

        IrBuilder.getInstance().setCurBlock(followBlock);
    }
}
