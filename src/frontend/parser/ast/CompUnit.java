package frontend.parser.ast;

import frontend.parser.ast.declaration.decl.Decl;
import frontend.parser.ast.function.single.FuncDef;
import frontend.parser.ast.function.single.MainFuncDef;

import java.util.ArrayList;

public class CompUnit implements SyntaxNode {
    private final SyntaxType type;
    private ArrayList<Decl> decls = null;
    private ArrayList<FuncDef> funcDefs = null;
    private final MainFuncDef mainFuncDef;

    public CompUnit(MainFuncDef mainFuncDef) {
        this.type = SyntaxType.COMP_UNIT;
        this.mainFuncDef = mainFuncDef;
    }

    public CompUnit(ArrayList<Decl> decls, ArrayList<FuncDef> funcDefs, MainFuncDef mainFuncDef) {
        this(mainFuncDef);
        this.decls = decls;
        this.funcDefs = funcDefs;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        for (Decl decl : decls) {
            sb.append(decl.syntaxInfoOutput());
        }
        for (FuncDef funcDef : funcDefs) {
            sb.append(funcDef.syntaxInfoOutput());
        }
        sb.append(mainFuncDef.syntaxInfoOutput());
        sb.append(type.getName()).append("\n");
        return sb.toString();
    }
}
