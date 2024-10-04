package frontend.lexer;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenStream;
import frontend.lexer.token.TokenType;
import middle.error.Error;
import middle.error.ErrorTable;
import middle.error.ErrorType;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.HashMap;

public class Lexer {
    private final PushbackInputStream stream;
    private int lineno;
    private char curChar;
    private final TokenStream tokenStream;
    private final ErrorTable errorTable;
    private static final HashMap<String, TokenType> words = new HashMap<>() {{
        put("main", TokenType.MAINTK);
        put("const", TokenType.CONSTTK);
        put("int", TokenType.INTTK);
        put("char", TokenType.CHARTK);
        put("break", TokenType.BREAKTK);
        put("continue", TokenType.CONTINUETK);
        put("if", TokenType.IFTK);
        put("else", TokenType.ELSETK);
        put("for", TokenType.FORTK);
        put("getint", TokenType.GETINTTK);
        put("getchar", TokenType.GETCHARTK);
        put("printf", TokenType.PRINTFTK);
        put("return", TokenType.RETURNTK);
        put("void", TokenType.VOIDTK);
    }};

    public Lexer(PushbackInputStream stream, ErrorTable errorTable) throws IOException {
        this.stream = stream;
        this.lineno = 1; // first line
        this.curChar = (char) stream.read(); // first char
        this.tokenStream = new TokenStream();
        this.errorTable = errorTable;
        analyse();
    }

    private void read() throws IOException { // read and update curChar
        curChar = (char) stream.read();
    }

    private void unread(char preChar) throws IOException { // unread and update curChar
        stream.unread(curChar); // unread
        curChar = preChar; // update
    }

    private boolean fileNotEnd() {
        return curChar != (char) -1;
    }

    private boolean endOfLine() {
        return curChar == '\n';
    }

    private void analyse() throws IOException {
        while (fileNotEnd()) {
            skipWhiteSpace();
            if (skipComment()) {
                continue;
            }
            Token token = getToken();
            if (token != null) {
                tokenStream.addToken(token);
            }
        }
    }

    private void skipWhiteSpace() throws IOException {
        while (fileNotEnd() && Character.isWhitespace(curChar)) {
            if (endOfLine()) {
                lineno++;
            }
            read();
        }
    }

    private boolean skipComment() throws IOException {
        if (curChar != '/') {
            return false;
        }
        char preChar = curChar;
        read();
        if (curChar == '/') { // type of //
            read();
            while (fileNotEnd() && !endOfLine()) {
                read();
            }
            if (endOfLine()) {
                lineno++;
                read();
            }
            return true;
        }
        if (curChar == '*') { // type of /* ... */
            read(); // endOfFile?
            if (endOfLine()) {
                lineno++;
            }
            char prevChar = curChar;
            read();
            while (fileNotEnd() && !(prevChar == '*' && curChar == '/')) {
                if (endOfLine()) {
                    lineno++;
                }
                prevChar = curChar;
                read();
            }
            read();
            return true;
        }
        unread(preChar);
        return false;
    }

    /**
     * int
     * word (reserved word or identifier)
     * Type 1: + - * / % ; , ( ) [ ] { }
     * Type 2: && ||
     * Type 3: < <= > >= ! != == =
     * char string
     */
    private Token getToken() throws IOException {
        if (Character.isDigit(curChar)) { // int
            return getIntToken();
        }
        if (Character.isLetter(curChar) || curChar == '_') { // word
            return getWordToken();
        }
        return switch (curChar) {
            case '+' -> getType1Token(TokenType.PLUS, "+");
            case '-' -> getType1Token(TokenType.MINU, "-");
            case '*' -> getType1Token(TokenType.MULT, "*");
            case '/' -> getType1Token(TokenType.DIV, "/");
            case '%' -> getType1Token(TokenType.MOD, "%");
            case ';' -> getType1Token(TokenType.SEMICN, ";");
            case ',' -> getType1Token(TokenType.COMMA, ",");
            case '(' -> getType1Token(TokenType.LPARENT, "(");
            case ')' -> getType1Token(TokenType.RPARENT, ")");
            case '[' -> getType1Token(TokenType.LBRACK, "[");
            case ']' -> getType1Token(TokenType.RBRACK, "]");
            case '{' -> getType1Token(TokenType.LBRACE, "{");
            case '}' -> getType1Token(TokenType.RBRACE, "}");

            case '&' -> getType2Token('&', TokenType.AND, "&&");
            case '|' -> getType2Token('|', TokenType.OR, "||");

            case '<' -> getType3Token("<", TokenType.LSS, TokenType.LEQ);
            case '>' -> getType3Token(">", TokenType.GRE, TokenType.GEQ);
            case '!' -> getType3Token("!", TokenType.NOT, TokenType.NEQ);
            case '=' -> getType3Token("=", TokenType.ASSIGN, TokenType.EQL);

            case '"' -> getStringToken(); // string
            case '\'' -> getCharToken(); // char

            default -> null;
        };
    }

    // + - * / % ; , ( ) [ ] { }
    private Token getType1Token(TokenType type, String content) throws IOException {
        read();
        return new Token(type, content, lineno);
    }

    // && ||
    private Token getType2Token(char c, TokenType type, String content) throws IOException {
        read();
        if (curChar != c) { // a-level error
            errorTable.addError(new Error(ErrorType.ILLEGAL_CHAR, lineno));
            return new Token(type, Character.toString(c), lineno);
        }
        read();
        return new Token(type, content, lineno);
    }

    // < <= > >= ! != == =
    private Token getType3Token(String preContent, TokenType preType, TokenType type)
            throws IOException {
        read();
        if (curChar == '=') { // <=, >=, !=, ==
            read();
            return new Token(type, preContent + "=", lineno);
        }
        return new Token(preType, preContent, lineno);
    }

    private Token getIntToken() throws IOException {
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(curChar)) {
            sb.append(curChar);
            read();
        }
        return new Token(TokenType.INTCON, sb.toString(), lineno);
    }

    private Token getCharToken() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(curChar); // '
        read();
        sb.append(curChar);
        if (curChar == '\'') {
            return new Token(TokenType.CHRCON, sb.toString(), lineno); // ''
        }
        if (curChar == '\\') {
            read();
            sb.append(curChar);
        }
        read();
        sb.append(curChar); // '
        read();
        return new Token(TokenType.CHRCON, sb.toString(), lineno);
    }

    private Token getStringToken() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(curChar); // "
        read();
        while (curChar != '"') {
            if (curChar == '\\') {
                sb.append(curChar);
                read();
            }
            sb.append(curChar);
            read();
        }
        sb.append(curChar); // "
        read();
        return new Token(TokenType.STRCON, sb.toString(), lineno);
    }

    private Token getWordToken() throws IOException { // reserved word or identifier
        StringBuilder sb = new StringBuilder();
        while (Character.isLetter(curChar) || Character.isDigit(curChar) || curChar == '_') {
            sb.append(curChar);
            read();
        }
        String content = sb.toString();
        TokenType type = words.getOrDefault(content, TokenType.IDENFR);
        return new Token(type, content, lineno);
    }

    public TokenStream getTokenStream() {
        return tokenStream;
    }
}
