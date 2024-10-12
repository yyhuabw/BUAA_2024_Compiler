package middle.error;

public enum ErrorType {
    // lexer
    ILLEGAL_CHAR("a"),

    // parser
    MISSING_SEMICN("i"),
    MISSING_R_PARENT("j"),
    MISSING_R_BRACK("k"),

    //
    REDEFINED_IDENT("b"),
    UNDEFINED_IDENT("c"),
    PARAM_NUM_MISMATCH("d"),
    PARAM_TYPE_MISMATCH("e"),
    VOID_MISMATCH_RETURN("f"),
    MISSING_RETURN("g"),
    ALTER_CONST("h"),
    PRINTF_FORMAT_MISMATCH("l"),
    MISUSE_BREAK_OR_CONTINUE("m");


    private final String code;

    ErrorType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
