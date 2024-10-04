package frontend.parser.ast.declaration.variable;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.declaration.type.BType;
import frontend.parser.ast.declaration.decl.DeclEle;
import frontend.parser.ast.declaration.variable.varDef.VarDef;

import java.util.ArrayList;

public class VarDecl implements DeclEle {
    private final SyntaxType type;
    private final BType bType;
    private final VarDef first;
    private ArrayList<Token> commas = null;
    private ArrayList<VarDef> varDefs = null;
    private final Token semicolon;

    public VarDecl(BType bType, VarDef first, Token semicolon) {
        this.type = SyntaxType.VAR_DECL;
        this.bType = bType;
        this.first = first;
        this.semicolon = semicolon;
    }

    public VarDecl(BType bType, VarDef first, ArrayList<Token> commas, ArrayList<VarDef> varDefs, Token semicolon) {
        this(bType, first, semicolon);
        this.commas = commas;
        this.varDefs = varDefs;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(bType.syntaxInfoOutput());
        sb.append(first.syntaxInfoOutput());
        for (int i = 0; i < commas.size(); i++) {
            sb.append(commas.get(i).syntaxInfoOutput());
            sb.append(varDefs.get(i).syntaxInfoOutput());
        }
        sb.append(semicolon.syntaxInfoOutput());
        sb.append(type.getName()).append("\n");
        return sb.toString();
    }
}
