package frontend.lexer;

import java.util.ArrayList;

public class TokenStream {
    private final ArrayList<Token> tokenList;
    private int curPos;

    public TokenStream() {
        this.tokenList = new ArrayList<>();
        this.curPos = -1;
    }

    public void addToken(Token token) {
        this.tokenList.add(token);
    }

    public Token getCurToken() {
        return tokenList.get(curPos);
    }

    public Token read() {
        if (curPos >= tokenList.size() - 1) {
            return null;
        }
        curPos++;
        return tokenList.get(curPos);
    }

    public void unread() {
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
