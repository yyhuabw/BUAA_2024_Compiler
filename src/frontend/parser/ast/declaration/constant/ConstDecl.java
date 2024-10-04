package frontend.parser.ast.declaration.constant;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.declaration.type.BType;
import frontend.parser.ast.declaration.decl.DeclEle;

import java.util.ArrayList;

public class ConstDecl implements DeclEle {
    private final SyntaxType type;
    private final Token constTk;
    private final BType bType;
    private final ConstDef first;
    private ArrayList<Token> commas = null;
    private ArrayList<ConstDef> constDefs = null;
    private final Token semicolon;

    public ConstDecl(Token constTk, BType bType, ConstDef first, Token semicolon) {
        this.type = SyntaxType.CONST_DECL;
        this.constTk = constTk;
        this.bType = bType;
        this.first = first;
        this.semicolon = semicolon;
    }

    public ConstDecl(Token constTk, BType bType, ConstDef first, ArrayList<Token> commas, ArrayList<ConstDef> constDefs, Token semicolon) {
        this(constTk, bType, first, semicolon);
        this.commas = commas;
        this.constDefs = constDefs;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(constTk.syntaxInfoOutput());
        sb.append(bType.syntaxInfoOutput());
        sb.append(first.syntaxInfoOutput());
        for (int i = 0; i < commas.size(); i++) {
            sb.append(commas.get(i).syntaxInfoOutput());
            sb.append(constDefs.get(i).syntaxInfoOutput());
        }
        sb.append(semicolon.syntaxInfoOutput());
        sb.append(type.getName()).append("\n");
        return sb.toString();
    }
}
