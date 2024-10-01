package frontend.parser.ast.expression.primaryExp;

import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.terminal.IntConst;

public class Number implements PrimaryExpEle {
    private final SyntaxType type;
    private final IntConst intConst;

    public Number(IntConst intConst) {
        this.type = SyntaxType.NUMBER;
        this.intConst = intConst;
    }

    @Override
    public String syntaxInfoOutput() {
        return intConst.syntaxInfoOutput() + type.getName() + "\n";
    }
}
