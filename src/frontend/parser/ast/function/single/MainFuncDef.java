package frontend.parser.ast.function.single;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.statement.block.Block;

public class MainFuncDef implements SyntaxNode {
    private final SyntaxType type;
    private final Token intTk;
    private final Token mainTk;
    private final Token leftParent;
    private final Token rightParent;
    private final Block block;

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

    @Override
    public String syntaxInfoOutput() {
        return intTk.syntaxInfoOutput() +
                mainTk.syntaxInfoOutput() +
                leftParent.syntaxInfoOutput() +
                rightParent.syntaxInfoOutput() +
                block.syntaxInfoOutput() +
                type.getName() + "\n";
    }
}
