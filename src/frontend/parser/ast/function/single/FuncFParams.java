package frontend.parser.ast.function.single;

import frontend.lexer.token.Token;
import frontend.parser.ast.SyntaxNode;
import frontend.parser.ast.SyntaxType;
import middle.llvm_ir.IrValue;
import middle.symbol.VarSymbol;

import java.util.ArrayList;

public class FuncFParams implements SyntaxNode {
    private final SyntaxType type;
    private final FuncFParam first;
    private ArrayList<Token> commas = null;
    private ArrayList<FuncFParam> funcFParams = null;

    public FuncFParams(FuncFParam first) {
        this.type = SyntaxType.FUNC_FORMAL_PARAMS;
        this.first = first;
    }

    public FuncFParams(FuncFParam first, ArrayList<Token> commas, ArrayList<FuncFParam> funcFParams) {
        this(first);
        this.commas = commas;
        this.funcFParams = funcFParams;
    }

    public ArrayList<VarSymbol> getSymbols() {
        ArrayList<VarSymbol> symbols = new ArrayList<>();
        symbols.add(first.getVarSymbol());
        for (FuncFParam funcFParam : funcFParams) {
            symbols.add(funcFParam.getVarSymbol());
        }
        return symbols;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(first.syntaxInfoOutput());
        for (int i = 0; i < commas.size(); i++) {
            sb.append(commas.get(i).syntaxInfoOutput());
            sb.append(funcFParams.get(i).syntaxInfoOutput());
        }
        sb.append(type.getName()).append("\n");
        return sb.toString();
    }

    // void
    @Override
    public IrValue genIR() {
        first.genIR();
        for (FuncFParam funcFParam : funcFParams) {
            funcFParam.genIR();
        }

        return null;
    }
}
