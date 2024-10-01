package frontend.parser.ast.expression.primaryExp;

import frontend.parser.ast.SyntaxType;
import frontend.parser.ast.terminal.CharConst;

public class Character implements PrimaryExpEle {
    private final SyntaxType type;
    private final CharConst charConst;

    public Character(CharConst charConst) {
        this.type = SyntaxType.CHARACTER;
        this.charConst = charConst;
    }

    @Override
    public String syntaxInfoOutput() {
        return charConst.syntaxInfoOutput() + type.getName() + "\n";
    }
}
