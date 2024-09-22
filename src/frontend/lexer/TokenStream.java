package frontend.lexer;

import java.util.ArrayList;

public class TokenStream {
    private final ArrayList<Token> tokenList;

    public TokenStream() {
        this.tokenList = new ArrayList<>();
    }

    public void addToken(Token token) {
        this.tokenList.add(token);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Token token : tokenList) {
            sb.append(token.getType()).append(" ").append(token.getContent()).append("\n");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
