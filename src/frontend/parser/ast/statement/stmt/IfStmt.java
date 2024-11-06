package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Cond;
import middle.llvm_ir.IrBasicBlock;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.jump.br.IrDirtBrInstr;

// 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
public class IfStmt implements StmtEle {
    private final Token ifTk;
    private final Token leftParent;
    private final Cond cond;
    private final Token rightParent;
    private final Stmt ifStmt;
    private Token elseTk = null;
    private Stmt elseStmt = null;

    public IfStmt(Token ifTk, Token leftParent, Cond cond, Token rightParent, Stmt ifStmt) {
        this.ifTk = ifTk;
        this.leftParent = leftParent;
        this.cond = cond;
        this.rightParent = rightParent;
        this.ifStmt = ifStmt;
    }

    public IfStmt(Token ifTk, Token leftParent, Cond cond, Token rightParent, Stmt ifStmt, Token elseTk, Stmt elseStmt) {
        this(ifTk, leftParent, cond, rightParent, ifStmt);
        this.elseTk = elseTk;
        this.elseStmt = elseStmt;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(ifTk.syntaxInfoOutput());
        sb.append(leftParent.syntaxInfoOutput());
        sb.append(cond.syntaxInfoOutput());
        sb.append(rightParent.syntaxInfoOutput());
        sb.append(ifStmt.syntaxInfoOutput());
        if (elseTk != null) {
            sb.append(elseTk.syntaxInfoOutput());
            sb.append(elseStmt.syntaxInfoOutput());
        }
        return sb.toString();
    }

    /**
     * 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
     * void
     * @return null
     */
    @Override
    public IrValue genIR() {
        IrBasicBlock ifTrueBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());

        if (elseTk != null) { // have ifFalseBlock
            IrBasicBlock ifFalseBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());
            IrBasicBlock followBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());

            // analyse condExp
            cond.genIRForCond(ifTrueBlock, ifFalseBlock);

            // analyse ifStmt and br to followBlock
            IrBuilder.getInstance().setCurBlock(ifTrueBlock);
            ifStmt.genIR();
            new IrDirtBrInstr(followBlock);

            // analyse elseStmt and br to followBlock
            IrBuilder.getInstance().setCurBlock(ifFalseBlock);
            elseStmt.genIR();
            new IrDirtBrInstr(followBlock);

            IrBuilder.getInstance().setCurBlock(followBlock);
        } else { // no ifFalseBlock
            IrBasicBlock followBlock = new IrBasicBlock(IrBuilder.getInstance().getBlockLabelName());

            // analyse condExp
            cond.genIRForCond(ifTrueBlock, followBlock);

            // analyse ifStmt and br to followBlock
            IrBuilder.getInstance().setCurBlock(ifTrueBlock);
            ifStmt.genIR();
            new IrDirtBrInstr(followBlock);

            IrBuilder.getInstance().setCurBlock(followBlock);
        }

        return null;
    }
}
