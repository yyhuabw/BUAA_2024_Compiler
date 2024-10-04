package frontend.lexer.token;

import frontend.parser.ast.SyntaxNode;

public class Token implements SyntaxNode {
    private final TokenType type;
    private final String content;
    private final int lineno;

    public Token(TokenType type, String content, int lineno) {
        this.type = type;
        this.content = content;
        this.lineno = lineno;
    }

    public TokenType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public int getLineno() {
        return lineno;
    }

    @Override
    public String syntaxInfoOutput() {
        return type + " " + content + "\n";
    }
}
