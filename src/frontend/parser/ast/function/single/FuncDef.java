package frontend.parser.ast.function.single;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.function.funcType.FuncType;
import frontend.parser.ast.statement.block.Block;
import frontend.parser.ast.terminal.Ident;

public class FuncDef implements SyntaxNode {
    private final SyntaxType type;
    private final FuncType funcType;
    private final Ident ident;
    private final Token leftParent;
    private FuncFParams funcFParams = null;
    private final Token rightParent;
    private final Block block;

    public FuncDef(FuncType funcType,
                   Ident ident,
                   Token leftParent,
                   Token rightParent,
                   Block block) {
        this.type = SyntaxType.FUNC_DEF;
        this.funcType = funcType;
        this.ident = ident;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
        this.block = block;
    }

    public FuncDef(FuncType funcType,
                   Ident ident,
                   Token leftParent,
                   FuncFParams funcFParams,
                   Token rightParent,
                   Block block) {
        this(funcType, ident, leftParent, rightParent, block);
        this.funcFParams = funcFParams;
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
}
