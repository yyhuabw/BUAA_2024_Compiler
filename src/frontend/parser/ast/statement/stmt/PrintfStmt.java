package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Exp;
import frontend.parser.ast.terminal.StringConst;

import java.util.ArrayList;

public class PrintfStmt implements StmtEle {
    private final Token printfTk;
    private final Token leftParent;
    private final StringConst stringConst;
    private ArrayList<Token> commas = null;
    private ArrayList<Exp> exps = null;
    private final Token rightParent;
    private final Token semicolon;

    public PrintfStmt(Token printfTk,
                      Token leftParent,
                      StringConst stringConst,
                      Token rightParent,
                      Token semicolon) {
        this.printfTk = printfTk;
        this.leftParent = leftParent;
        this.stringConst = stringConst;
        this.rightParent = rightParent;
        this.semicolon = semicolon;
    }

    public PrintfStmt(Token printfTk,
                      Token leftParent,
                      StringConst stringConst,
                      ArrayList<Token> commas,
                      ArrayList<Exp> exps,
                      Token rightParent,
                      Token semicolon) {
        this(printfTk, leftParent, stringConst, rightParent, semicolon);
        this.commas = commas;
        this.exps = exps;
    }

    @Override
    public String syntaxInfoOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(printfTk.syntaxInfoOutput());
        sb.append(leftParent.syntaxInfoOutput());
        sb.append(stringConst.syntaxInfoOutput());
        for (int i = 0; i < commas.size(); i++) {
            sb.append(commas.get(i).syntaxInfoOutput());
            sb.append(exps.get(i).syntaxInfoOutput());
        }
        sb.append(rightParent.syntaxInfoOutput());
        sb.append(semicolon.syntaxInfoOutput());
        return sb.toString();
    }
}
