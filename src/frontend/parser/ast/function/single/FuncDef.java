package frontend.parser.ast.function.single;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.function.funcType.FuncType;
import frontend.parser.ast.statement.block.Block;
import frontend.parser.ast.terminal.Ident;
import middle.symbol.FuncSymbol;
import middle.symbol.SymbolManager;
import middle.symbol.value.ValueType;

public class FuncDef implements SyntaxNode {
    private final SyntaxType type;
    private final FuncType funcType;
    private final Ident ident;
    private Token leftParent;
    private FuncFParams funcFParams = null;
    private Token rightParent;
    private Block block;
    private FuncSymbol funcSymbol = null;

    public FuncDef(FuncType funcType, Ident ident) {
        this.type = SyntaxType.FUNC_DEF;
        this.funcType = funcType;
        this.ident = ident;
    }

    public void setAttributes(Token leftParent, Token rightParent, Block block) {
        this.leftParent = leftParent;
        this.rightParent = rightParent;
        this.block = block;
    }

    public void setFuncFParams(FuncFParams funcFParams) {
        this.funcFParams = funcFParams;
        funcSymbol.setSymbols(funcFParams.getSymbols());
    }

    public boolean addToSTAndCheck() {
        String name = ident.getToken().getContent();
        ValueType returnType = funcType.getReturnType();
        FuncSymbol funcSymbol = new FuncSymbol(name, returnType);
        this.funcSymbol = funcSymbol;
        return SymbolManager.getInstance().addAndCheck(funcSymbol);
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
