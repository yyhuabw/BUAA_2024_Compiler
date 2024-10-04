package frontend.parser.ast.statement.block;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.statement.stmt.StmtEle;

import java.util.ArrayList;

public class Block implements StmtEle {
    private final SyntaxType type;
    private final Token leftBrace;
    private ArrayList<BlockItem> blockItems = null;
    private final Token rightBrace;

    public Block(Token leftBrace, Token rightBrace) {
        this.type = SyntaxType.BLOCK;
        this.leftBrace = leftBrace;
        this.rightBrace = rightBrace;
    }

    public Block(Token leftBrace, ArrayList<BlockItem> blockItems, Token rightBrace) {
        this(leftBrace, rightBrace);
        this.blockItems = blockItems;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(leftBrace.syntaxInfoOutput());
        for (BlockItem blockItem : blockItems) {
            sb.append(blockItem.syntaxInfoOutput());
        }
        sb.append(rightBrace.syntaxInfoOutput());
        sb.append(type.getName()).append("\n");
        return sb.toString();
    }
}
