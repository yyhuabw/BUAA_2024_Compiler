package frontend.parser.ast.statement.stmt;

import frontend.lexer.token.Token;
import frontend.parser.ast.expression.single.Exp;
import frontend.parser.ast.terminal.StringConst;
import middle.llvm_ir.IrBuilder;
import middle.llvm_ir.IrValue;
import middle.llvm_ir.instruction.jump.io.IrPutchInstr;
import middle.llvm_ir.instruction.jump.io.IrPutintInstr;
import middle.llvm_ir.instruction.jump.io.IrPutstrInstr;
import middle.llvm_ir.type.IrIntType;
import middle.llvm_ir.utils.IrStrLiteral;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 'printf''('StringConst {','Exp}')'';'
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

    public boolean isWrongFormat() {
        Pattern pattern = Pattern.compile("%[dc]");
        Matcher matcher = pattern.matcher(stringConst.getContent());
        int cnt = 0;
        while (matcher.find()) {
            cnt++;
        }

        return exps.size() != cnt;
    }

    public int getLineno() {
        return printfTk.getLineno();
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

    /**
     * void
     * @return null
     */
    @Override
    public IrValue genIR() {
        String str = stringConst.getFixedContent();
        StringBuilder sb = new StringBuilder(); // for strLiteral output
        int expIndex = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '%' &&
                    (str.charAt(i+1) == 'd' || str.charAt(i+1) == 'c')) {
                if (!sb.isEmpty()) { // output and clear
                    IrStrLiteral strLiteral = new IrStrLiteral(IrBuilder.getInstance().getStrLiteralName(), sb.toString());
                    new IrPutstrInstr(strLiteral);
                    sb.setLength(0);
                }
                if (str.charAt(i+1) == 'd') { // %d
                    IrValue expIR = exps.get(expIndex++).genVarIR(IrIntType.INT32);
                    new IrPutintInstr(expIR);
                } else { // %c
                    IrValue expIR = exps.get(expIndex++).genVarIR(IrIntType.INT32);
                    new IrPutchInstr(expIR);
                }
                i++; // skip %d | %c
            } else if (str.charAt(i) == '\\') { // only have \n
                sb.append('\n');
                i++; // skip \n
            } else {
                sb.append(str.charAt(i));
            }
        }
        if (!sb.isEmpty()) {
            IrStrLiteral strLiteral = new IrStrLiteral(IrBuilder.getInstance().getStrLiteralName(), sb.toString());
            new IrPutstrInstr(strLiteral);
        }

        return null;
    }
}
